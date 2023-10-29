package ku.cs.model;

import ku.cs.entity.Products;
import ku.cs.utility.EntityUtility;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;

public class Product implements Row {

    private HashMap<String, Object> data = EntityUtility.getMap(Products.getSqlTable());

    public Product() {
        this("null", 0);
    }

    public Product(String product_name, int size){
        this(product_name, size, -1);
    }

    public Product(String product_name, int size, double progress_rate){
        data.put("product_name", product_name);
        data.put("size", size);
        data.put("progress_rate", progress_rate);
    }

    public Product(HashMap<String, Object> data){
        setData(data);
    }

    @Override
    public HashMap<String, Object> getData() {
        return data;
    }

    @Override
    public void setData(HashMap<String, Object> data) {
        this.data = data;
    }

    public String getId() {
        return (String) data.get("product_id");
    }

    public void setId(String product_id) {
        if (!EntityUtility.isIdValid(Products.getSqlTable(), product_id)) throw new RuntimeException("Product[setId]: Invalid product_id -> " + product_id);
        data.put("product_id", product_id);
    }

    public String getName() {
        return (String) data.get("product_name");
    }

    public void setName(String product_name) {
        data.put("product_name", product_name);
    }

    public int getSize() {
        return (int) data.get("size");
    }

    public void setSize(int size) {
        data.put("size", size);
    }

    public double getProgressRate() {
        return (double) data.get("progress_rate");
    }

    public void setProgressRate(double progress_rate) {
        data.put("progress_rate", progress_rate);
    }

    public void load(int id) throws SQLException {
        load(EntityUtility.idFormatter(Products.getSqlTable(), id));
    }

    @Override
    public void load(String primaryKeys) throws SQLException {
//        if(Products.getData() == null) Products.load();
        boolean cannotLoad;
        try {
            cannotLoad = Products.isNew(primaryKeys);
        }
        catch (RuntimeException e) {
            cannotLoad = true;
        }
        cannotLoad = cannotLoad || !EntityUtility.isIdValid(Products.getSqlTable(), primaryKeys);
        if (cannotLoad) throw new RuntimeException("Product[load]: Can't load product with primaryKeys -> " + primaryKeys);
        setData(Products.getData().get(primaryKeys).getData());
    }

    @Override
    public int save() throws SQLException, ParseException {
        return Products.save(this);
    }

    @Override
    public int delete() throws SQLException, ParseException {
        return Products.delete(this);
    }

    @Override
    public HashMap<String, Object> getPrimaryKeys() {
        HashMap<String, Object> primaryKeys = new HashMap<>();
        for (SQLColumn sqlColumn : Products.getSqlTable().getPrimaryKeys()) {
            primaryKeys.put(sqlColumn.getName(), data.get(sqlColumn.getName()));
        }
        return primaryKeys;
    }

    @Override
    public String toString() {
        return "Object-Product: " + data.toString();
    }
}
