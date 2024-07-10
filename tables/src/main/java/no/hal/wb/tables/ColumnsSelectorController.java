package no.hal.wb.tables;

import java.util.ArrayList;
import java.util.List;

import org.controlsfx.control.CheckComboBox;

import tech.tablesaw.api.Table;
import tech.tablesaw.columns.Column;

public class ColumnsSelectorController {

    private final CheckComboBox<String> columnsSelector;
    private final ColumnFilter columnFilter;
    
    public ColumnsSelectorController(CheckComboBox<String> columnsSelector, ColumnFilter columnFilter) {
        this.columnsSelector = columnsSelector;
        this.columnFilter = columnFilter;
    }

    public void setTable(Table table) {
        if (table == null) {
            columnsSelector.getItems().clear();
        } else {
            var columnNames = table.columns().stream()
                .filter(columnFilter)
                .map(Column::name)
                .toList();
            var selectorItems = columnsSelector.getItems();
            if (columnNames.equals(selectorItems)) {
                return;
            }
            var checkModel = columnsSelector.getCheckModel();
            List<String> selectedColumnNames = new ArrayList<String>(checkModel.getCheckedItems());
            selectorItems.setAll(columnNames);
            // try to preserve the selected columns
            for (String selectedColumnName : selectedColumnNames) {
                if (columnNames.contains(selectedColumnName) && (! checkModel.isChecked(selectedColumnName))) {
                    checkModel.check(selectedColumnName);
                }
            }
        }
    }
}
