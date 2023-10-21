package ku.cs.entity;

import ku.cs.model.SQLColumn;
import ku.cs.model.SQLTable;

import java.sql.Timestamp;

public class DailyRecords implements Entity {
    private Timestamp date;
    private Works works;
    private int amount;

    private static final ku.cs.model.SQLTable sqlTable = new SQLTable("DailyRecords");
    static {
        SQLColumn sqlColumn;

        sqlColumn = new SQLColumn();
        sqlColumn.setName("for_work");
        sqlColumn.setType("varchar(255)");
        sqlColumn.setPrimaryKey();
        sqlColumn.setForeignKey();
        sqlColumn.setForeignKeyTable("Works");
        sqlColumn.setForeignKeyColumn("work_id");
        sqlColumn.setOnDelete("CASCADE");
        sqlTable.addColumObj(sqlColumn);

        sqlColumn = new SQLColumn();
        sqlColumn.setName("date");
        sqlColumn.setType("date");
        sqlColumn.setNotNull();
        sqlColumn.setPrimaryKey();
        sqlTable.addColumObj(sqlColumn);

        sqlColumn = new SQLColumn();
        sqlColumn.setName("amount");
        sqlColumn.setType("int");
        sqlColumn.setNotNull();
        sqlTable.addColumObj(sqlColumn);
    }

    public static SQLTable getSqlTable() {
        return sqlTable;
    }
}
