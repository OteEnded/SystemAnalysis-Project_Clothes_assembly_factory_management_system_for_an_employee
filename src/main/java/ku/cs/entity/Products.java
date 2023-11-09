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

public class Products {

    private static final SQLTable sqlTable = new SQLTable("Products");
    static {
        SQLColumn sqlColumn;

        sqlColumn = new SQLColumn();
        sqlColumn.setName("product_id");
        sqlColumn.setType("varchar(255)");
        sqlColumn.setPrimaryKey();
        sqlTable.addColumObj(sqlColumn);

        sqlColumn = new SQLColumn();
        sqlColumn.setName("product_name");
        sqlColumn.setType("varchar(255)");
        sqlColumn.setNotNull();
        sqlTable.addColumObj(sqlColumn);

        sqlColumn = new SQLColumn();
        sqlColumn.setName("size");
        sqlColumn.setType("int");
        sqlColumn.setNotNull();
        sqlTable.addColumObj(sqlColumn);

        sqlColumn = new SQLColumn();
        sqlColumn.setName("progress_rate");
        sqlColumn.setType("double");
        sqlTable.addColumObj(sqlColumn);
    }

    public static SQLTable getSqlTable() {
        return sqlTable;
    }

    private static HashMap<String, Product> data;

    public static HashMap<String, Product> getData() throws SQLException{
        if(data == null) load();
        return data;
    }

    public static List<Product> getDataAsList() throws SQLException {
        if(data == null) load();
        return toList(data);
    }

    public static List<Product> toList(HashMap<String, Product> data) {
        return new ArrayList<>(data.values());
    }

    public static void setData(HashMap<String, Product> data) {
        Products.data = data;
    }

    public static HashMap<String, Product> load() throws SQLException {
        return load(true);
    }

    // getAll data from database
    public static HashMap<String, Product> load(boolean updateBuffer) throws SQLException {

        try {
            PopUpUtility.popUp("loading", "Products (สินค้า)");
        } catch (Exception e){
            ProjectUtility.debug("Products[getAll]: cannot do pop ups thing");
            ProjectUtility.debug(e);
        }

        HashMap<String, Product> dataFromDB = new HashMap<>();
        List<SQLRow> sqlRows = sqlTable.getAll();
        for (SQLRow sqlRow : sqlRows) {
            dataFromDB.put(sqlRow.getJoinedPrimaryKeys(), new Product(sqlRow.getValuesMap()));
        }
        if (updateBuffer) data = dataFromDB;

        try {
            PopUpUtility.close("loading", true);
        } catch (Exception e){
            ProjectUtility.debug(e);
        }

        return dataFromDB;
    }

    public static String getJoinedPrimaryKeys(Product product) {
        SQLRow sqlRow = new SQLRow(sqlTable, product);
        return sqlRow.getJoinedPrimaryKeys();
    }

    public static String getNewId() throws SQLException {
        if (data == null) load();
        if (data.isEmpty()) return EntityUtility.idFormatter(sqlTable, 1);
        ArrayList<String> oldId = new ArrayList<>(getData().keySet());
        Collections.sort(oldId);
        int oldLastId = Integer.parseInt((oldId.get(getData().size() - 1).substring(1,6)));
        return EntityUtility.idFormatter(sqlTable, oldLastId + 1);
    }

    public static void addData(Product product) throws SQLException {
        if (data == null) load();
        ProjectUtility.debug("Products[addData]: adding product ->", product);
        if (product.getId() == null) product.setId(getNewId());
        data.put(getJoinedPrimaryKeys(product), product);
        ProjectUtility.debug("Products[addData]: added product with primaryKeys ->", getJoinedPrimaryKeys(product), "=", product);
    }

    public static boolean isNew(Product product) throws SQLException {
        return isNew(product.getId());
    }

    public static boolean isNew(String primaryKeys) throws SQLException {
        if (data == null) load();
        if (data.isEmpty()) return true;
        return !data.containsKey(primaryKeys);
    }

    public static boolean isProductValid(Product product) {
        return verifyProduct(product).size() == 0;
    }

    public static List<String> verifyProduct(Product product) {
        List<String> error = new ArrayList<>(EntityUtility.verifyRowByTable(sqlTable, product));
        return error;
    }

    public static int save(Product product) throws SQLException, ParseException {
        ProjectUtility.debug("Products[save]: saving product ->", product);
        if(!isProductValid(product)) throw new RuntimeException("Products[save]: product's data is not valid ->" + verifyProduct(product));
        if (isNew(product)) {
            addData(product);
            return DataSourceDB.exeUpdatePrepare(sqlTable.getInsertQuery(new SQLRow(sqlTable, product)));
        }

        if (data.get(getJoinedPrimaryKeys(product)).getProgressRate() == -1 && product.getProgressRate() != -1) {
            Works.addFilter("product", product.getId());
            for (Work work : Works.getFilteredData().values()) {
                work.setNote(work.getNote().replace(Works.note_waitForUserEstimate, ""));
                work.save();
            }
        }

        data.put(getJoinedPrimaryKeys(product), product);
        return DataSourceDB.exeUpdatePrepare(sqlTable.getUpdateQuery(new SQLRow(sqlTable, product)));
    }

    public static int delete(String id) throws SQLException, ParseException {
        Product product = new Product();
        product.load(id);
        return delete(product);
    }

    public static int delete(Product product) throws SQLException, ParseException {
        ProjectUtility.debug("Products[delete]: deleting product ->", product);
        if (isNew(product)) throw new RuntimeException("Products[delete]: Can't delete product that is not in database");
        data.remove(getJoinedPrimaryKeys(product));
        int affectedRow = DataSourceDB.exeUpdatePrepare(sqlTable.getDeleteQuery(new SQLRow(sqlTable, product)));
        Works.load();
        MaterialUsages.load();
        return affectedRow;
    }

    public static HashMap<String, Object> filter;
    public static void addFilter(String column, Object value) {
        if (filter == null) filter = new HashMap<>();
        filter.put(column, value);
    }
    public static void setFilter(HashMap<String, Object> filter) {
        Products.filter = filter;
    }
    public static HashMap<String, Object> getFilter() {
        return filter;
    }
    public static HashMap<String, Product> getFilteredData(HashMap<String, Object> filter) throws SQLException {
        setFilter(filter);
        return getFilteredData();
    }
    public static HashMap<String, Product> getFilteredData() throws SQLException {
        if (filter == null) throw new RuntimeException("Products[getFilteredData]: filter is null, Please set filter first or get all data without filter using -> Products.getData()");
        HashMap<String, Product> filteredData = new HashMap<>();
        try {
            for (SQLRow sqlRow: sqlTable.getWhere(filter)) {
                filteredData.put(sqlRow.getJoinedPrimaryKeys(), new Product(sqlRow.getValuesMap()));
            }
        }
        catch (ParseException e){
            e.printStackTrace();
            throw new RuntimeException("products[getFilteredData]: ParseException");
        }
        return filteredData;
    }

    public static List<Product> getFilteredDataSortedBy(String column){
        return null;
    }

    public static List<Product> getSortedBy(String column) throws SQLException {
        if (data == null) load();
        return getSortedBy(column, data);
    }

    public static List<Product> getSortedBy(String column, HashMap<String, Product> data) throws SQLException {
        List<Product> products = toList(data);
        products.sort((o1, o2) -> {
            try {
                return o1.getData().get(column).toString().compareTo(o2.getData().get(column).toString());
            } catch (RuntimeException e) {
                return 0;
            }
        });
        return products;
    }
}
