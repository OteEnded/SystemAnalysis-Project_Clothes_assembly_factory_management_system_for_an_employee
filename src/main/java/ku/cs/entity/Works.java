package ku.cs.entity;

import ku.cs.common.Status;
import ku.cs.common.WorkType;
import ku.cs.model.SQLColumn;
import ku.cs.model.SQLTable;

import java.sql.Timestamp;

public class Works implements Entity {
    private String id;
    private Timestamp deadline;
    private Timestamp start_date;
    private WorkType work_type;
    private Status status;
    private int goal_amount;
    private int progress_amount;
    private String note;
    private Products products;

    private static final SQLTable sqlTable = new SQLTable("Works");
    static {
        SQLColumn sqlColumn;

        sqlColumn = new SQLColumn();
        sqlColumn.setName("work_id");
        sqlColumn.setType("varchar(255)");
        sqlColumn.setPrimaryKey();
        sqlTable.addColumObj(sqlColumn);

        sqlColumn = new SQLColumn();
        sqlColumn.setName("work_type");
        sqlColumn.setType("varchar(255)");
        sqlColumn.setNotNull();
        sqlTable.addColumObj(sqlColumn);

        sqlColumn = new SQLColumn();
        sqlColumn.setName("product");
        sqlColumn.setType("varchar(255)");
        sqlColumn.setForeignKey();
        sqlColumn.setForeignKeyTable("Products");
        sqlColumn.setForeignKeyColumn("product_id");
        sqlColumn.setOnDelete("SET NULL");
        sqlTable.addColumObj(sqlColumn);

        sqlColumn = new SQLColumn();
        sqlColumn.setName("deadline");
        sqlColumn.setType("date");
        sqlColumn.setNotNull();
        sqlTable.addColumObj(sqlColumn);

        sqlColumn = new SQLColumn();
        sqlColumn.setName("create_date");
        sqlColumn.setType("date");
        sqlColumn.setNotNull();
        sqlTable.addColumObj(sqlColumn);

        sqlColumn = new SQLColumn();
        sqlColumn.setName("start_date");
        sqlColumn.setType("date");
        sqlTable.addColumObj(sqlColumn);

        sqlColumn = new SQLColumn();
        sqlColumn.setName("status");
        sqlColumn.setType("varchar(255)");
        sqlColumn.setDefaultValue("'รอรับงาน'");
        sqlColumn.setNotNull();
        sqlTable.addColumObj(sqlColumn);

        sqlColumn = new SQLColumn();
        sqlColumn.setName("goal_amount");
        sqlColumn.setType("int");
        sqlColumn.setNotNull();
        sqlTable.addColumObj(sqlColumn);

        sqlColumn = new SQLColumn();
        sqlColumn.setName("progress_amount");
        sqlColumn.setType("int");
        sqlColumn.setDefaultValue("0");
        sqlColumn.setNotNull();
        sqlTable.addColumObj(sqlColumn);

        sqlColumn = new SQLColumn();
        sqlColumn.setName("note");
        sqlColumn.setType("varchar(255)");
        sqlTable.addColumObj(sqlColumn);

        sqlColumn = new SQLColumn();
        sqlColumn.setName("repair_work");
        sqlColumn.setType("varchar(255)");
        sqlColumn.setForeignKey();
        sqlColumn.setForeignKeyTable("Works");
        sqlColumn.setForeignKeyColumn("work_id");
        sqlColumn.setOnDelete("CASCADE");
        sqlTable.addColumObj(sqlColumn);
    }

    public static SQLTable getSqlTable() {
        return sqlTable;
    }
}
