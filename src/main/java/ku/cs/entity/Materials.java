package ku.cs.entity;

import ku.cs.model.*;
import ku.cs.service.DataSourceDB;
import ku.cs.utility.EntityUtility;
import ku.cs.utility.PopUpUtility;
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

    public static HashMap<String, Material> getData() throws SQLException{
        if(data == null) load();
        return data;
    }

    public static List<Material> getDataAsList() throws SQLException {
        if(data == null) load();
        return toList(data);
    }

    public static List<Material> toList(HashMap<String, Material> data) {
        return new ArrayList<>(data.values());
    }

    public static void setData(HashMap<String, Material> data) {
        Materials.data = data;
    }

    public static HashMap<String, Material> load() throws SQLException {
        return load(true);
    }

    public static HashMap<String, Material> load(boolean updateBuffer) throws SQLException {

        try {
            PopUpUtility.popUp("loading", "Materials (วัตถุดิบ)");
        } catch (Exception e){
            ProjectUtility.debug("Materials[load]: cannot do pop ups thing");
            ProjectUtility.debug(e);
        }

        HashMap<String, Material> dataFromDB = new HashMap<>();
        List<SQLRow> sqlRows = DataSourceDB.load(sqlTable);
        for (SQLRow sqlRow : sqlRows) {
            dataFromDB.put(sqlRow.getJoinedPrimaryKeys(), new Material(sqlRow.getValuesMap()));
        }
        if (updateBuffer) data = dataFromDB;

        try {
            PopUpUtility.close("loading", true);
        } catch (Exception e){
            ProjectUtility.debug("Materials[load]: cannot do pop ups thing");
            ProjectUtility.debug(e);
        }

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

    public static boolean isMaterialValid(Material material) {
        return verifyMaterial(material).size() == 0;
    }

    public static List<String> verifyMaterial(Material material) {
        List<String> error = new ArrayList<>(EntityUtility.verifyRowByTable(sqlTable, material));
        return error;
    }

    public static int save(Material material) throws SQLException, ParseException {
        ProjectUtility.debug("Materials[save]: saving material ->", material);
        if (!isMaterialValid(material)) throw new RuntimeException("Materials[save]: material's data is not valid ->" + verifyMaterial(material));
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
        int affectedRow =  DataSourceDB.exePrepare(sqlTable.getDeleteQuery(new SQLRow(sqlTable, material)));
        MaterialUsages.load();
        return affectedRow;
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
                if (material.getData().get(column) == null) {
                    isFiltered = false;
                    break;
                }
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

    public static List<Material> getSortedBy(String column) throws SQLException {
        if (data == null) load();
        return getSortedBy(column, data);
    }

    public static List<Material> getSortedBy(String column, HashMap<String, Material> data) throws SQLException {
//        ProjectUtility.debug("Materials[getSortedBy]: getting data sorted by ->", column);
//        if (data == null) throw new RuntimeException("Materials[getSortedBy]: data is null, Please load data first or get all data without filter using -> Materials.getData()");
//        List<String> sortedValues = new ArrayList<String>();
//        for (Material material : data.values()) {
//            sortedValues.add(material.getData().get(column).toString());
//        }
//        Collections.sort(sortedValues);
//        ProjectUtility.debug("Materials[getSortedBy]: sorted target ->", sortedValues);
//        List<Material> sortedMaterial = new ArrayList<>();
//        for (String sortedValue : sortedValues) {
//            addFilter(column, ProjectUtility.castStringToObject(sortedValue, sqlTable.getColumnByName(column).getClassType()));
//            sortedMaterial.addAll(getFilteredData().values());
//        }
//        return sortedMaterial;
        List<Material> materials = toList(data);
        materials.sort((o1, o2) -> {
            try {
                return o1.getData().get(column).toString().compareTo(o2.getData().get(column).toString());
            } catch (RuntimeException e) {
                return 0;
            }
        });
        return materials;
    }
}
