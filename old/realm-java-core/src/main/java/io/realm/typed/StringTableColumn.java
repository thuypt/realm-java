package io.realm.typed;

import io.realm.Table;
import io.realm.TableOrView;
//import TableQuery;

/**
 * Type of the fields that represent a string column in the generated XyzTable
 * class for the Xyz entity.
 */
public class StringTableColumn<Cursor, View, Query> extends StringTableOrViewColumn<Cursor, View, Query> {

    protected final Table table;

    public StringTableColumn(EntityTypes<?, View, Cursor, Query> types, TableOrView table, int index, String name) {
        super(types, table, index, name);
        this.table = (Table)table;
    }

    /*public StringTableColumn(EntityTypes<?, View, Cursor, Query> types, TableOrView table, TableQuery query, int index,
            String name) {
        super(types, table, query, index, name);
        this.table = (Table)table;
    }*/

    public void setIndex() {
        table.setIndex(columnIndex);
    }

    public boolean hasIndex() {
        return table.hasIndex(columnIndex);
    }
}