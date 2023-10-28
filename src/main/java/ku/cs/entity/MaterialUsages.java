package ku.cs.entity;

import ku.cs.model.SQLColumn;
import ku.cs.model.SQLTable;

public class MaterialUsages {

    private static final SQLTable sqlTable = new SQLTable("MaterialUsages");
    static {
        SQLColumn sqlColumn;

        sqlColumn = new SQLColumn();
        sqlColumn.setName("product_id");
        sqlColumn.setType("varchar(255)");
        sqlColumn.setPrimaryKey();
        sqlColumn.setForeignKey();
        sqlColumn.setForeignKeyTable("Products");
        sqlColumn.setForeignKeyColumn("product_id");
        sqlColumn.setOnDelete("CASCADE");
        sqlTable.addColumObj(sqlColumn);

        sqlColumn = new SQLColumn();
        sqlColumn.setName("material_id");
        sqlColumn.setType("varchar(255)");
        sqlColumn.setPrimaryKey();
        sqlColumn.setForeignKey();
        sqlColumn.setForeignKeyTable("Materials");
        sqlColumn.setForeignKeyColumn("material_id");
        sqlColumn.setOnDelete("CASCADE");
        sqlTable.addColumObj(sqlColumn);

        sqlColumn = new SQLColumn();
        sqlColumn.setName("amount");
        sqlColumn.setType("int");
        sqlColumn.setNotNull();
        sqlTable.addColumObj(sqlColumn);

        sqlColumn = new SQLColumn();
        sqlColumn.setName("yield");
        sqlColumn.setType("int");
        sqlColumn.setDefaultValue("1");
        sqlColumn.setNotNull();
        sqlTable.addColumObj(sqlColumn);
    }

    public static SQLTable getSqlTable() {
        return sqlTable;
    }
}
