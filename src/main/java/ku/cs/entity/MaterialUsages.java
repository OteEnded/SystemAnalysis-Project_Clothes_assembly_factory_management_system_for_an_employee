package ku.cs.entity;

import ku.cs.model.*;
import ku.cs.service.DataSourceDB;
import ku.cs.utility.EntityUtility;
import ku.cs.utility.PopUpUtility;
import ku.cs.utility.ProjectUtility;

import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
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
    private static boolean isDataDirty = true;
    public static void setDirty() {
        isDataDirty = true;
    }

    public static HashMap<String, MaterialUsage> getData() throws SQLException{
        return load();
    }

    public static List<MaterialUsage> getDataAsList() throws SQLException{
        return toList(load());
    }

    public static List<MaterialUsage> toList(HashMap<String, MaterialUsage> data) {
        return new ArrayList<>(data.values());
    }

    public static HashMap<String, MaterialUsage> load() throws SQLException {
        return load(true);
    }

    // getAll data from database
    public static HashMap<String, MaterialUsage> load(boolean updateBuffer) throws SQLException {
        if (!isDataDirty) return data;
        try {
            PopUpUtility.popUp("loading", "MaterialUsages (การใช้วัตถุดิบ)");
        } catch (Exception e){
            ProjectUtility.debug("MaterialUsages[getAll]: cannot do pop ups thing");
            ProjectUtility.debug(e);
        }
        HashMap<String, MaterialUsage> dataFromDB = new HashMap<>();
        List<SQLRow> sqlRows = sqlTable.getAll();
        for (SQLRow sqlRow : sqlRows) {
            dataFromDB.put(sqlRow.getJoinedPrimaryKeys(), new MaterialUsage(sqlRow.getValuesMap()));
        }
        if (updateBuffer) {
            data = dataFromDB;
            isDataDirty = false;
        }
        try {
            PopUpUtility.close("loading", true);
        } catch (Exception e){
            ProjectUtility.debug(e);
        }
        return dataFromDB;
    }

    public static String getJoinedPrimaryKeys(MaterialUsage materialUsage) {
        SQLRow sqlRow = new SQLRow(sqlTable, materialUsage);
        ProjectUtility.debug(sqlRow);
        return sqlRow.getJoinedPrimaryKeys();
    }

    public static boolean isNew(MaterialUsage materialUsage) throws SQLException {
        return isNew(getJoinedPrimaryKeys(materialUsage));
    }

    public static boolean isNew(String primaryKeys) throws SQLException {
        if (data == null) load();
        if (data.isEmpty()) return true;
        ProjectUtility.debug(data);
        return !data.containsKey(primaryKeys);
    }

    public static boolean isMaterialUsageValid(MaterialUsage materialUsage) {
        return verifyMaterialUsage(materialUsage).size() == 0;
    }

    public static List<String> verifyMaterialUsage(MaterialUsage materialUsage) {
        return new ArrayList<>(EntityUtility.verifyRowByTable(sqlTable, materialUsage));
    }

    public static int save(MaterialUsage materialUsage) throws SQLException, ParseException {
        ProjectUtility.debug("MaterialUsages[save]: saving materialUsage: " + materialUsage.getData());
        if (!isMaterialUsageValid(materialUsage)) throw new RuntimeException("MaterialUsages[save]: materialUsage is not valid -> " + verifyMaterialUsage(materialUsage));
        setDirty();
        if (isNew(materialUsage)) {
            return DataSourceDB.exeUpdatePrepare(sqlTable.getInsertQuery(new SQLRow(sqlTable, materialUsage)));
        }
        return DataSourceDB.exeUpdatePrepare(sqlTable.getUpdateQuery(new SQLRow(sqlTable, materialUsage)));
    }

    public static int delete(MaterialUsage materialUsage) throws SQLException, ParseException {
        ProjectUtility.debug("MaterialUsages[delete]: deleting materialUsage: " + materialUsage.getData());
        ProjectUtility.debug(data);
        if (isNew(materialUsage)) throw new RuntimeException("MaterialUsages[delete]: Can't delete materialUsage that is not in database");
        setDirty();
        return DataSourceDB.exeUpdatePrepare(sqlTable.getDeleteQuery(new SQLRow(sqlTable, materialUsage)));
    }

    public static HashMap<String, Object> filter;
    public static void addFilter(String column, Object value) {
        if (filter == null) filter = new HashMap<>();
        filter.put(column, value);
    }
    public static void setFilter(HashMap<String, Object> filter) {
        MaterialUsages.filter = filter;
    }
    public static HashMap<String, Object> getFilter() {
        return filter;
    }

    public static MaterialUsage find(String primaryKeys) throws SQLException {
        String[] keys = primaryKeys.split("\\|");
        return find(keys[0], keys[1]);
    }

    public static MaterialUsage find(String product_id, String material_id) throws SQLException {
        setFilter(null);
        addFilter("product_id", product_id);
        addFilter("material_id", material_id);
        return find();
    }

    public static MaterialUsage find(HashMap<String, Object> filter) throws SQLException {
        setFilter(filter);
        return find();
    }

    public static MaterialUsage find() throws SQLException {
        try {
            return new MaterialUsage(sqlTable.getFindOne(filter).getValuesMap());
        }
        catch (ParseException e) {
            throw new RuntimeException(e);
        }
        finally {
            filter = null;
        }
    }

    public static HashMap<String, MaterialUsage> getFilteredData(HashMap<String, Object> filter) throws SQLException {
        setFilter(filter);
        return getFilteredData();
    }

    public static HashMap<String, MaterialUsage> getFilteredData() throws SQLException {
        ProjectUtility.debug("MaterialUsages[getFilteredData]: getting data with filter ->", filter);
        if (filter == null)
            throw new RuntimeException("MaterialUsages[getFilteredData]: filter is null, Please set filter first or get all data without filter using -> MaterialUsages.getData()");
        HashMap<String, MaterialUsage> filteredData = new HashMap<>();
        try {
            for (SQLRow sqlRow : sqlTable.getWhere(filter)) {
                filteredData.put(sqlRow.getJoinedPrimaryKeys(), new MaterialUsage(sqlRow.getValuesMap()));
            }
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("MaterialUsages[getFilteredData]: ParseException");
        }
        return filteredData;
    }

    public static List<MaterialUsage> getSortedBy(String column) throws SQLException {
        if (data == null) load();
        return getSortedBy(column, data);
    }

    public static List<MaterialUsage> getSortedBy(String column, HashMap<String, MaterialUsage> data) throws SQLException {
        List<MaterialUsage> materialUsages = toList(data);
        materialUsages.sort((o1, o2) -> {
            try {
                return o1.getData().get(column).toString().compareTo(o2.getData().get(column).toString());
            } catch (RuntimeException e) {
                return 0;
            }
        });
        return materialUsages;
    }
}