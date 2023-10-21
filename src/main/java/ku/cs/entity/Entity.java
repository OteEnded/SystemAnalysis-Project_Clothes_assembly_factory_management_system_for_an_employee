package ku.cs.entity;

import ku.cs.model.SQLTable;

public interface Entity {
    SQLTable SQLTable = new SQLTable(null);

    static SQLTable getSqlTable() {
        return SQLTable;
    }
}
