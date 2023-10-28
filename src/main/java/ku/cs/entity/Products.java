package ku.cs.entity;

import ku.cs.model.*;
import ku.cs.service.DataSourceDB;
import ku.cs.utility.EntityUtility;
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

    private static HashMap<String, Product> data = null;
    static {
        data = new HashMap<>();
    }

    public static HashMap<String, Product> getData() {
        return data;
    }

    public static void setData(HashMap<String, Product> data) {
        Products.data = data;
    }

    public static HashMap<String, Product> load() throws SQLException {
        return load(true);
    }

    // load data from database
    public static HashMap<String, Product> load(boolean updateProducts) throws SQLException {
        HashMap<String, Product> dataFromDB = new HashMap<>();
        List<SQLRow> sqlRows = DataSourceDB.load(sqlTable);
        for (SQLRow sqlRow : sqlRows) {
            dataFromDB.put(sqlRow.getJoinedPrimaryKeys(), new Product(sqlRow.getValuesMap()));
        }
        if (updateProducts) data = dataFromDB;
        return dataFromDB;
    }

    public String getJoinedPrimaryKeys(Product product) {
        SQLRow sqlRow = new SQLRow(sqlTable, product);
        return sqlRow.getJoinedPrimaryKeys();
    }

    public static String getNewId() throws SQLException {
        if (data == null) load();
        if (data.size() == 0) return EntityUtility.idFormatter(sqlTable, 1);
        ArrayList<String> oldId = new ArrayList<>(getData().keySet());
        Collections.sort(oldId);
        int oldLastId = Integer.parseInt((oldId.get(getData().size() - 1).substring(1,6)));
        return EntityUtility.idFormatter(sqlTable, oldLastId + 1);
    }

    public static void addData(Product product) throws SQLException {
        ProjectUtility.debug("Products[addData]: adding product ->", product);
        if (data == null) load();
        if (product.getId() == null) product.setId(getNewId());
        data.put(product.getId(), product);
        ProjectUtility.debug("Products[addData]: added product with id ->", product.getId(), "=", product);
    }

    public static boolean isNew(Product product) throws SQLException {
        return isNew(product.getId());
    }

    public static boolean isNew(String id) throws SQLException {
        load();
        if (data == null) return true;
        return !data.containsKey(id);
    }

    public static int save(Product product) throws SQLException, ParseException {
        ProjectUtility.debug("Products[save]: saving product ->", product);

        if (isNew(product)) {
            addData(product);
            return DataSourceDB.exePrepare(sqlTable.getInsertQuery(new SQLRow(sqlTable, product)));
        }
        else {
            data.put(product.getId(), product);
            return DataSourceDB.exePrepare(sqlTable.getUpdateQuery(new SQLRow(sqlTable, product)));
        }
    }

    public static int delete(String id) throws SQLException, ParseException {
        Product product = new Product();
        product.load(id);
        return delete(product);
    }

    public static int delete(Product product) throws SQLException, ParseException {
        ProjectUtility.debug("Products[delete]: deleting product ->", product);
        if (isNew(product)) throw new RuntimeException("Products[delete]: Can not delete product that is not in database");
        data.remove(product.getId());
        return DataSourceDB.exePrepare(sqlTable.getDeleteQuery(new SQLRow(sqlTable, product)));
    }




}
