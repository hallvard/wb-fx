package no.hal.wb.tables;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.controlsfx.control.CheckComboBox;

import jakarta.enterprise.context.Dependent;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import no.hal.fx.bindings.BindingTarget;
import no.hal.fx.bindings.BindingsTarget;
import no.hal.wb.tables.TableController.TableUpdate;
import tech.tablesaw.api.NumberColumn;
import tech.tablesaw.api.Row;
import tech.tablesaw.api.Table;

@Dependent
public class XYChartViewController implements BindingsTarget {

    @FXML
    XYChart xyChart;

    @FXML
    CategoryAxis mainAxis;

    @FXML
    NumberAxis numberAxis;

    @FXML
    ComboBox<String> mainAxisColumnSelector;
    
    private ColumnSelectorController mainAxisColumnSelectorController;

    @FXML
    CheckComboBox<String> numberColumnsSelector;
    
    private ColumnsSelectorController numberColumnsSelectorController;

    private Property<TableUpdate> tableProperty = new SimpleObjectProperty<TableUpdate>();

    private List<BindingTarget<?>> bindingTargets;

    @Override
    public List<BindingTarget<?>> getBindingTargets() {
        return this.bindingTargets;
    }

    @FXML
    void initialize() {
        mainAxisColumnSelectorController = new ColumnSelectorController(mainAxisColumnSelector, new ColumnFilter(), true);
        numberColumnsSelectorController = new ColumnsSelectorController(numberColumnsSelector, new ColumnFilter(NumberColumn.class));
        numberColumnsSelector.getCheckModel().getCheckedItems().subscribe(this::updateChart);
        tableProperty.subscribe(this::tableUpdated);
        this.bindingTargets = List.of(
            new BindingTarget<TableUpdate>(xyChart, TableUpdate.class, tableProperty)
        );
    }

    private void tableUpdated() {
        Table table = tableProperty.getValue().table();
        mainAxisColumnSelectorController.setTable(table);
        numberColumnsSelectorController.setTable(table);
        updateChart();
    }

    @FXML
    void updateChart() {
        xyChart.getData().clear();
        String mainAxisColumn = mainAxisColumnSelector.getValue();
        List<String> numberColumns = numberColumnsSelector.getCheckModel().getCheckedItems();
        if (mainAxisColumn == null || mainAxisColumn.isBlank() || numberColumns.isEmpty()) {
            return;
        }
        Table table = tableProperty.getValue().table();
        xyChart.setTitle(table.name());
        mainAxis.setLabel(mainAxisColumn);
        
        Map<String, XYChart.Series> seriesMap = new HashMap<>();
        for (var numberColumn : numberColumns) {
            XYChart.Series<?, ?> series = new XYChart.Series<>();
            series.setName(numberColumn);
            seriesMap.put(numberColumn, series);
        }
        Row row = table.row(0);
        while (row.hasNext()) {
            var mainAxisValue = String.valueOf(row.getObject(mainAxisColumn));
            for (var numberColumn : numberColumns) {
                var value = row.getNumber(numberColumn);
                var data = (xyChart.getXAxis() == mainAxis ? new XYChart.Data(mainAxisValue, value) : new XYChart.Data(value, mainAxisValue));
                seriesMap.get(numberColumn).getData().add(data);
            }
            row = row.next();
        }
        xyChart.getData().setAll(seriesMap.values());
    }
}
