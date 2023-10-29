package ku.cs.entity;

import ku.cs.model.MaterialUsage;
import ku.cs.model.SQLColumn;
import ku.cs.model.SQLRow;
import ku.cs.model.SQLTable;
import ku.cs.service.DataSourceDB;
import ku.cs.utility.ProjectUtility;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

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

    private static HashMap<String, MaterialUsage> data;
    static {
        data = new HashMap<>();
    }

    public static HashMap<String, MaterialUsage> getData() {
        return data;
    }

    public static void setData(HashMap<String, MaterialUsage> data) {
        MaterialUsages.data = data;
    }

    public static HashMap<String, MaterialUsage> load() throws SQLException {
        return load(true);
    }

    // load data from database
    public static HashMap<String, MaterialUsage> load(boolean updateBuffer) throws SQLException {
        HashMap<String, MaterialUsage> dataFromDB = new HashMap<>();
        List<SQLRow> sqlRows = DataSourceDB.load(sqlTable);
        for (SQLRow sqlRow : sqlRows) {
            dataFromDB.put(sqlRow.getJoinedPrimaryKeys(), new MaterialUsage(sqlRow.getValuesMap()));
        }
        if (updateBuffer) data = dataFromDB;
        return dataFromDB;
    }

    public static String getJoinedPrimaryKeys(MaterialUsage materialUsage) {
        SQLRow sqlRow = new SQLRow(sqlTable, materialUsage);
        return sqlRow.getJoinedPrimaryKeys();
    }

    public static void addData(MaterialUsage materialUsage) {
        ProjectUtility.debug("MaterialUsages[addData]: adding materialUsage->", materialUsage);
        data.put(getJoinedPrimaryKeys(materialUsage), materialUsage);
        ProjectUtility.debug("Product[addData]: added materialUsage with primaryKeys ->", getJoinedPrimaryKeys(materialUsage), "=", materialUsage);
    }

    public static boolean isNew(MaterialUsage materialUsage) throws SQLException {
        return isNew(getJoinedPrimaryKeys(materialUsage));
    }

    public static boolean isNew(String primaryKeys) throws SQLException {
        if (data == null) load();
        if (data.isEmpty()) return true;
        return !data.containsKey(primaryKeys);
    }

    public static int save(MaterialUsage materialUsage) throws SQLException, ParseException {
        ProjectUtility.debug("MaterialUsages[save]: saving materialUsage: " + materialUsage.getData());
        if (isNew(materialUsage)) {
            addData(materialUsage);
            return DataSourceDB.exePrepare(sqlTable.getInsertQuery(new SQLRow(sqlTable, materialUsage)));
        }
        data.put(getJoinedPrimaryKeys(materialUsage), materialUsage);
        return DataSourceDB.exePrepare(sqlTable.getUpdateQuery(new SQLRow(sqlTable, materialUsage)));
    }

    public static int delete(MaterialUsage materialUsage) throws SQLException, ParseException {
        ProjectUtility.debug("MaterialUsages[delete]: deleting materialUsage: " + materialUsage.getData());
        if (isNew(materialUsage)) throw new RuntimeException("MaterialUsages[delete]: Can't delete materialUsage that is not in database");
        data.remove(getJoinedPrimaryKeys(materialUsage));
        return DataSourceDB.exePrepare(sqlTable.getDeleteQuery(new SQLRow(sqlTable, materialUsage)));
    }
}
