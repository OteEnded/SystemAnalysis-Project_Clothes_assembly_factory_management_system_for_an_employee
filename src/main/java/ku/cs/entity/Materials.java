package ku.cs.entity;

import ku.cs.model.SQLColumn;
import ku.cs.model.SQLRow;
import ku.cs.model.SQLTable;
import ku.cs.model.Material;
import ku.cs.service.DataSourceDB;
import ku.cs.utility.EntityUtility;
import ku.cs.utility.ProjectUtility;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Materials {

    private static final SQLTable sqlTable = new SQLTable("Materials");
    static {
        SQLColumn sqlColumn;

        sqlColumn = new SQLColumn();
        sqlColumn.setName("material_id");
        sqlColumn.setType("varchar(255)");
        sqlColumn.setPrimaryKey();
        sqlTable.addColumObj(sqlColumn);

        sqlColumn = new SQLColumn();
        sqlColumn.setName("material_name");
        sqlColumn.setType("varchar(255)");
        sqlColumn.setNotNull();
        sqlTable.addColumObj(sqlColumn);

        sqlColumn = new SQLColumn();
        sqlColumn.setName("unit_name");
        sqlColumn.setType("varchar(255)");
        sqlColumn.setNotNull();
        sqlTable.addColumObj(sqlColumn);
    }

    public static SQLTable getSqlTable() {
        return sqlTable;
    }

    private static HashMap<String, Material> data;
    static {
        data = new HashMap<>();
    }

    public static HashMap<String, Material> getData() {
        return data;
    }

    public static void setData(HashMap<String, Material> data) {
        Materials.data = data;
    }

    public static HashMap<String, Material> load() throws SQLException {
        return load(true);
    }

    public static HashMap<String, Material> load(boolean updateBuffer) throws SQLException {
        HashMap<String, Material> dataFromDB = new HashMap<>();
        List<SQLRow> sqlRows = DataSourceDB.load(sqlTable);
        for (SQLRow sqlRow : sqlRows) {
            dataFromDB.put(sqlRow.getJoinedPrimaryKeys(), new Material(sqlRow.getValuesMap()));
        }
        if (updateBuffer) data = dataFromDB;
        return dataFromDB;
    }

    public static String getJoinedPrimaryKeys(Material material) {
        SQLRow sqlRow = new SQLRow(sqlTable, material);
        return sqlRow.getJoinedPrimaryKeys();
    }

    public static String getNewId() throws SQLException {
        if (data == null) load();
        if (data.isEmpty()) return EntityUtility.idFormatter(sqlTable, 1);
        ArrayList<String> oldId = new ArrayList<>(getData().keySet());
        Collections.sort(oldId);
        int oldLastId = Integer.parseInt(oldId.get(oldId.size() - 1).substring(1,6));
        return EntityUtility.idFormatter(sqlTable, oldLastId + 1);
    }

    public static void addData(Material material) throws SQLException{
        ProjectUtility.debug("Materials[addData]: adding material ->", material);
        if (data == null) load();
        if (material.getId() == null) material.setId(getNewId());
        data.put(getJoinedPrimaryKeys(material), material);
        ProjectUtility.debug("Materials[addData]: added material with primaryKeys ->", getJoinedPrimaryKeys(material), "=", material);
    }

    public static boolean isNew(Material material) throws SQLException {
        return isNew(material.getId());
    }

    public static boolean isNew(String primaryKeys) throws SQLException {
        if (data == null) load();
        if (data.isEmpty()) return true;
        return !data.containsKey(primaryKeys);
    }

    public static int save(Material material) throws SQLException, ParseException {
        ProjectUtility.debug("Materials[save]: saving material ->", material);
        if (isNew(material)) {
            addData(material);
            return DataSourceDB.exePrepare(sqlTable.getInsertQuery(new SQLRow(sqlTable, material)));
        }
        data.put(getJoinedPrimaryKeys(material), material);
        return DataSourceDB.exePrepare(sqlTable.getUpdateQuery(new SQLRow(sqlTable, material)));
    }

    public static int delete(String id) throws SQLException, ParseException {
        Material material = new Material();
        material.load(id);
        return delete(material);
    }

    public static int delete(Material material) throws SQLException, ParseException {
        ProjectUtility.debug("Materials[delete]: deleting material ->", material);
        if (isNew(material)) throw new RuntimeException("Materials[delete]: Can not delete material that is not in database");
        data.remove(getJoinedPrimaryKeys(material));
        return DataSourceDB.exePrepare(sqlTable.getDeleteQuery(new SQLRow(sqlTable, material)));
    }

    public static HashMap<String, Object> filter;
    public static void addFilter(String column, Object value) {
        if (filter == null) filter = new HashMap<>();
        filter.put(column, value);
    }
    public static void setFilter(HashMap<String, Object> filter) {
        Materials.filter = filter;
    }
    public static HashMap<String, Object> getFilter() {
        return filter;
    }
    public static HashMap<String, Material> getFilteredData(HashMap<String, Object> filter) throws SQLException {
        setFilter(filter);
        return getFilteredData();
    }
    public static HashMap<String, Material> getFilteredData() throws SQLException{
        if (filter == null) throw new RuntimeException("Materials[getFilteredData]: filter is null, Please set filter first or get all data without filter using -> Materials.getData()");
        if (data == null) load();
        HashMap<String, Material> filteredData = new HashMap<>();
        for (Material material: getData().values()) {
            boolean isFiltered = true;
            for (String column: filter.keySet()) {
                if(!material.getData().get(column).equals(filter.get(column))) {
                    isFiltered = false;
                    break;
                }
            }
            if (isFiltered) filteredData.put(material.getId(), material);
        }
        filter = null;
        return filteredData;
    }
}
