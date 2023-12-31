package ku.cs.model;

import ku.cs.entity.MaterialUsages;
import ku.cs.entity.Materials;
import ku.cs.entity.Products;
import ku.cs.utility.EntityUtility;
import ku.cs.utility.ProjectUtility;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;

public class MaterialUsage implements Row {

    private HashMap<String, Object> data = EntityUtility.getMap(MaterialUsages.getSqlTable());

    public MaterialUsage() {

    }

    public MaterialUsage(String material_id, String product_id, int amount){
        this(material_id, product_id, amount, 1);
    }

    public MaterialUsage(String material_id, String product_id, int amount, int yield){
        data.put("material_id", material_id);
        data.put("product_id", product_id);
        data.put("amount", amount);
        data.put("yield", yield);
    }

    public MaterialUsage(HashMap<String, Object> data){
        setData(data);
    }

    @Override
    public void setData(HashMap<String, Object> data) {
        this.data = data;
    }

    @Override
    public HashMap<String, Object> getData() {
        return data;
    }

    public String getProductId() {
        return (String) data.get("product_id");
    }

    public void setProductId(String product_id) {
        data.put("product_id", product_id);
    }

    public void setProduct(Product product) {
        setProductId(product.getId());
    }

    public Product getProduct() throws SQLException {
        Product product = new Product();
        product.load((String) data.get("product_id"));
        return product;
    }

    public String getMaterialId() {
        return (String) data.get("material_id");
    }

    public void setMaterialId(String material_id) {
        data.put("material_id", material_id);
    }

    public void setMaterial(Material material) {
        setMaterialId(material.getId());
    }

    public Material getMaterial() throws SQLException {
        Material material = new Material();
        material.load((String) data.get("material_id"));
        return material;
    }

    public int getAmount() {
        return (int) data.get("amount");
    }

    public void setAmount(int amount) {
        data.put("amount", amount);
    }

    public int getYield() {
        return (int) data.get("yield");
    }

    public void setYield(int yield) {
        data.put("yield", yield);
    }

    public double getAmountPerYield() {
        return getAmount() / (double) getYield();
    }

    public int getExpectedMaterialUsedByWork(Work work) {
        return (int) Math.ceil(getAmountPerYield() * work.getGoalAmount());
    }

    public void load(int product_id, int material_id) throws SQLException {
        load(EntityUtility.idFormatter(Products.getSqlTable(), product_id), EntityUtility.idFormatter(Materials.getSqlTable(), material_id));
    }

    public void load(Product product, Material material) throws SQLException {
        load(product.getId(), material.getId());
    }

    public void load(String product_id, String material_id) throws SQLException {
        load(String.join("|", product_id, material_id));
    }

    @Override
    public void load(String primaryKeys) throws SQLException {
        if (MaterialUsages.isNew(primaryKeys)) throw new RuntimeException("MaterialUsages[load]: Cannot load MaterialUsage with primaryKeys -> " + primaryKeys);
        setData(MaterialUsages.find(primaryKeys).getData());
    }

    @Override
    public int save() throws SQLException, ParseException {
        return MaterialUsages.save(this);
    }

    @Override
    public int delete() throws SQLException, ParseException {
        return MaterialUsages.delete(this);
    }

    @Override
    public HashMap<String, Object> getPrimaryKeys() {
        HashMap<String, Object> primaryKeys = new HashMap<>();
        for (SQLColumn sqlColumn : MaterialUsages.getSqlTable().getPrimaryKeys()) {
            primaryKeys.put(sqlColumn.getName(), data.get(sqlColumn.getName()));
        }
        return primaryKeys;
    }

    @Override
    public String toString() {
        return "Object-MaterialUsage: " + data.toString();
    }
}
