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
    private static boolean isDataDirty = true;
    public static void setDirty() {
        isDataDirty = true;
    }

    public static HashMap<String, Material> getData() throws SQLException{
        return load();
    }

    public static List<Material> getDataAsList() throws SQLException {
        return toList(load());
    }

    public static List<Material> toList(HashMap<String, Material> data) {
        return new ArrayList<>(data.values());
    }

    public static HashMap<String, Material> load() throws SQLException {
        return load(true);
    }

    public static HashMap<String, Material> load(boolean updateBuffer) throws SQLException {
        if (!isDataDirty) return data;
        try {
            PopUpUtility.popUp("loading", "Materials (วัตถุดิบ)");
        } catch (Exception e){
            ProjectUtility.debug("Materials[getAll]: cannot do pop ups thing");
            ProjectUtility.debug(e);
        }
        HashMap<String, Material> dataFromDB = new HashMap<>();
        List<SQLRow> sqlRows = sqlTable.getAll();
        for (SQLRow sqlRow : sqlRows) {
            dataFromDB.put(sqlRow.getJoinedPrimaryKeys(), new Material(sqlRow.getValuesMap()));
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

    public static String getJoinedPrimaryKeys(Material material) {
        SQLRow sqlRow = new SQLRow(sqlTable, material);
        return sqlRow.getJoinedPrimaryKeys();
    }

    public static String getNewId() throws SQLException {
        return EntityUtility.getNewId(sqlTable);
    }

    public static boolean isNew(Material material) throws SQLException {
        if (material == null) throw new RuntimeException("Materials[isNew]: material is null");
        if (material.getId() == null) throw new RuntimeException("Materials[isNew]: material's id is null");
        return isNew(material.getId());
    }

    public static boolean isNew(String primaryKeys) throws SQLException {
        Materials.addFilter("material_id", primaryKeys);
        return Materials.getFilteredData().isEmpty();
    }

    public static boolean isMaterialValid(Material material) {
        return verifyMaterial(material).size() == 0;
    }

    public static List<String> verifyMaterial(Material material) {
        return new ArrayList<>(EntityUtility.verifyRowByTable(sqlTable, material));
    }

    public static int save(Material material) throws SQLException, ParseException {
        ProjectUtility.debug("Materials[save]: saving material ->", material);
        if (!isMaterialValid(material)) throw new RuntimeException("Materials[save]: material's data is not valid ->" + verifyMaterial(material));
        setDirty();
        if (isNew(material)) {
            material.setId(getNewId());
            return DataSourceDB.exeUpdatePrepare(sqlTable.getInsertQuery(new SQLRow(sqlTable, material)));
        }
        return DataSourceDB.exeUpdatePrepare(sqlTable.getUpdateQuery(new SQLRow(sqlTable, material)));
    }

    public static int delete(String id) throws SQLException, ParseException {
        Material material = new Material();
        material.load(id);
        return delete(material);
    }

    public static int delete(Material material) throws SQLException, ParseException {
        ProjectUtility.debug("Materials[delete]: deleting material ->", material);
        if (isNew(material)) throw new RuntimeException("Materials[delete]: Can not delete material that is not in database");
        setDirty();
        int affectedRow =  DataSourceDB.exeUpdatePrepare(sqlTable.getDeleteQuery(new SQLRow(sqlTable, material)));
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

    public static Material find(String id) throws SQLException {
        addFilter("material_id", id);
        return find();
    }

    public static Material find(HashMap<String, Object> filter) throws SQLException {
        setFilter(filter);
        return find();
    }

    public static Material find() throws SQLException {
        try {
            return new Material(sqlTable.getFindOne(filter).getValuesMap());
        }
        catch (ParseException e) {
            throw new RuntimeException(e);
        }
        finally {
            filter = null;
        }
    }

    public static HashMap<String, Material> getFilteredData(HashMap<String, Object> filter) throws SQLException {
        setFilter(filter);
        return getFilteredData();
    }
    public static HashMap<String, Material> getFilteredData() throws SQLException{
        if (filter == null) throw new RuntimeException("Materials[getFilteredData]: filter is null, Please set filter first or get all data without filter using -> Materials.getData()");
        HashMap<String, Material> filteredData = new HashMap<>();
        try {
            for (SQLRow sqlRow: sqlTable.getWhere(filter)) {
                filteredData.put(sqlRow.getJoinedPrimaryKeys(), new Material(sqlRow.getValuesMap()));
            }
        }
        catch (ParseException e){
            e.printStackTrace();
            throw new RuntimeException("Materials[getFilteredData]: ParseException");
        }
        return filteredData;
    }

    public static List<Material> getSortedBy(String column) throws SQLException {
        if (data == null) load();
        return getSortedBy(column, data);
    }

    public static List<Material> getSortedBy(String column, HashMap<String, Material> data) throws SQLException {
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
