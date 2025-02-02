package org.datavec.dataframe.filtering;

import org.datavec.dataframe.api.ColumnType;
import org.datavec.dataframe.api.IntColumn;
import org.datavec.dataframe.api.LongColumn;
import org.datavec.dataframe.api.ShortColumn;
import org.datavec.dataframe.api.Table;
import org.datavec.dataframe.columns.Column;
import org.datavec.dataframe.columns.ColumnReference;
import org.datavec.dataframe.util.Selection;

/**
 */
public class IntLessThanOrEqualTo extends ColumnFilter {

    private int value;

    public IntLessThanOrEqualTo(ColumnReference reference, int value) {
        super(reference);
        this.value = value;
    }

    public Selection apply(Table relation) {
        String name = columnReference.getColumnName();
        Column column = relation.column(name);
        ColumnType type = column.type();
        switch (type) {
            case INTEGER:
                IntColumn intColumn = relation.intColumn(name);
                return intColumn.isLessThanOrEqualTo(value);
            case LONG_INT:
                LongColumn longColumn = relation.longColumn(name);
                return longColumn.isLessThanOrEqualTo(value);
            case SHORT_INT:
                ShortColumn shortColumn = relation.shortColumn(name);
                return shortColumn.isLessThanOrEqualTo(value);
            default:
                throw new UnsupportedOperationException("Columns of type " + type.name()
                                + " do not support the operation " + "lessThanOrEqualTo(anInt) ");
        }
    }
}
