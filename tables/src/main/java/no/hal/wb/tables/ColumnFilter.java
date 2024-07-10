package no.hal.wb.tables;

import java.util.function.Predicate;

import tech.tablesaw.api.ColumnType;
import tech.tablesaw.columns.Column;

public record ColumnFilter(Class<? extends Column> columnClass, ColumnType columnType) implements Predicate<Column<?>> {

    public ColumnFilter(Class<? extends Column> columnClass) {
        this(columnClass, null);
    }
    public ColumnFilter(ColumnType columnType) {
        this(null, columnType);
    }
    public ColumnFilter() {
        this(null, null);
    }

    @Override
    public boolean test(Column<?> col) {
        return ((columnClass == null || columnClass.isInstance(col)) && (columnType == null || col.type() == columnType));
    }
}
