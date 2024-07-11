package no.hal.wb.tables;

import java.util.List;
import java.util.Map;

import org.controlsfx.control.CheckComboBox;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import no.hal.expressions.ExpressionSupport;
import no.hal.fx.bindings.BindingSource;
import no.hal.fx.bindings.BindingTarget;
import no.hal.fx.bindings.BindingsSource;
import no.hal.fx.bindings.BindingsTarget;
import no.hal.wb.tables.TableController.TableUpdate;
import tech.tablesaw.aggregate.AggregateFunction;
import tech.tablesaw.aggregate.AggregateFunctions;
import tech.tablesaw.api.CategoricalColumn;
import tech.tablesaw.api.Table;

@Dependent
public class TableSummaryViewController extends AbstractTableViewController implements BindingsTarget, BindingsSource {

    @FXML
    CheckComboBox<String> summaryColumnsSelector;

    private ColumnsSelectorController summaryColumnsSelectorController;

    @FXML
    ComboBox<String> aggregateFunctionSelector;

    @FXML
    CheckComboBox<String> byColumnsSelector;

    private ColumnsSelectorController byColumnsSelectorController;

    private Map<String, AggregateFunction> aggregateFunctions = Map.of(
        "Count", AggregateFunctions.count,
        "Sum", AggregateFunctions.sum,
        "Average", AggregateFunctions.mean,
        "Minimum", AggregateFunctions.min,
        "Maximum", AggregateFunctions.max
    );

    @FXML
    TableView<Integer> tableView;

    @Override
    protected TableView<Integer> getTableView() {
        return tableView;
    }

    private Property<TableUpdate> tableProperty = new SimpleObjectProperty<TableUpdate>();

    private List<BindingTarget<?>> bindingTargets;

    @Override
    public List<BindingTarget<?>> getBindingTargets() {
        return this.bindingTargets;
    }

    private List<BindingSource<?>> bindingSources;

    @Override
    public List<BindingSource<?>> getBindingSources() {
        return bindingSources;
    }

    @Inject
    @Named("mvel")
    ExpressionSupport mvelExpressionSupport;

    @Override
    protected ExpressionSupport getExpressionSupport() {
        return mvelExpressionSupport;
    }

    @FXML
    void initialize() {
        super.initialize();
        summaryColumnsSelectorController = new ColumnsSelectorController(summaryColumnsSelector, new ColumnFilter());
        summaryColumnsSelector.getCheckModel().getCheckedItems().subscribe(this::updateSummary);
    
        if (aggregateFunctionSelector.getItems().isEmpty()) {
            aggregateFunctionSelector.getItems().setAll(aggregateFunctions.keySet());
            aggregateFunctionSelector.getSelectionModel().select("Count");
        } else {
            aggregateFunctionSelector.getSelectionModel().selectFirst();
        }
    
        byColumnsSelectorController = new ColumnsSelectorController(byColumnsSelector, new ColumnFilter());
        byColumnsSelector.getCheckModel().getCheckedItems().subscribe(this::updateSummary);
    
        tableProperty.subscribe(this::tableUpdated);
        this.bindingTargets = List.of(
            new BindingTarget<TableUpdate>(getTableView(), TableUpdate.class, tableProperty)
        );
        bindingSources = List.of(createTableBindingSource());
        Platform.runLater(this::tableUpdated);
    }

    private Table getTable() {
        var tableUpdate = tableProperty.getValue();
        return tableUpdate != null ? tableUpdate.table() : null;
    }

    private void tableUpdated() {
        Table table = getTable();
        summaryColumnsSelectorController.setTable(table);
        byColumnsSelectorController.setTable(table);
        updateSummary();
    }

    @FXML
    void updateSummary() {
        List<String> summaryColumns = summaryColumnsSelector.getCheckModel().getCheckedItems();
        List<String> byColumns = byColumnsSelector.getCheckModel().getCheckedItems();
        if (summaryColumns.isEmpty() ) {
            return;
        }
        Table table = getTable();
        if (table == null) {
            return;
        }    
        var summary = table.summarize(summaryColumns, aggregateFunctions.get(aggregateFunctionSelector.getSelectionModel().getSelectedItem()));
        var summaryTable = (byColumns.isEmpty() ? summary.apply() : summary.by(byColumns.toArray(new String[byColumns.size()])));
        setTable(summaryTable);
    }
}
