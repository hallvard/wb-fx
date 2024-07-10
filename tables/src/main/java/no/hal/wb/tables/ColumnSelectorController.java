package no.hal.wb.tables;

import java.util.ArrayList;

import javafx.scene.control.ComboBox;
import tech.tablesaw.api.Table;
import tech.tablesaw.columns.Column;

public class ColumnSelectorController {

    private final ComboBox<String> columnSelector;
    private final ColumnFilter columnFilter;
    boolean mandatory;
    
    public ColumnSelectorController(ComboBox<String> columnSelector, ColumnFilter columnFilter, boolean mandatory) {
        this.columnSelector = columnSelector;
        this.columnFilter = columnFilter;
        this.mandatory = mandatory;
    }
    public ColumnSelectorController(ComboBox<String> columnSelector, ColumnFilter columnFilter) {
        this(columnSelector, columnFilter, true);
    }

    public void setTable(Table table) {
        if (table == null) {
            columnSelector.getItems().clear();
        } else {
            var columnNames = table.columns().stream()
            .filter(columnFilter)
            .map(Column::name)
            .toList();
            if (! mandatory) {
                columnNames = new ArrayList<>(columnNames);
                columnNames.add(0, "");
            }
            var selectorItems = columnSelector.getItems();
            if (columnNames.equals(selectorItems)) {
                return;
            }
            String selectedColumnName = columnSelector.getValue();
            selectorItems.setAll(columnNames);
            // try to preserve the selected column
            if (columnNames.contains(selectedColumnName) && (! selectedColumnName.equals(columnSelector.getSelectionModel().getSelectedItem()))) {
                columnSelector.setValue(selectedColumnName);
            }                
        }
    }
}
