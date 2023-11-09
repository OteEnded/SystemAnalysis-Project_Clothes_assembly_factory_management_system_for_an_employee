package ku.cs.model;

import ku.cs.entity.Materials;
import ku.cs.utility.EntityUtility;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;

public class Material implements Row{
    private HashMap<String, Object> data = EntityUtility.getMap(Materials.getSqlTable());

    public Material() {
        this("null", "null");
    }

    public Material(String material_name, String unit_name){
        data.put("material_name", material_name);
        data.put("unit_name", unit_name);
    }

    public Material(HashMap<String, Object> data){
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
        return (String) data.get("material_id");
    }

    public void setId(String material_id) {
        if (!EntityUtility.isIdValid(Materials.getSqlTable(), material_id)) throw new RuntimeException("Material[setId]: Invalid material_id -> " + material_id);
        data.put("material_id", material_id);
    }

    public String getName() {
        return (String) data.get("material_name");
    }

    public void setName(String material_name) {
        data.put("material_name", material_name);
    }

    public String getUnitName() {
        return (String) data.get("unit_name");
    }

    public void setUnitName(String unit_name) {
        data.put("unit_name", unit_name);
    }

    public void load(int id) throws SQLException {
        load(EntityUtility.idFormatter(Materials.getSqlTable(), id));
    }

    @Override
    public void load(String primaryKeys) throws SQLException {
        EntityUtility.checkId(Materials.getSqlTable(), primaryKeys);
        setId(primaryKeys);
        if (Materials.isNew(this)) throw new RuntimeException("Material[load]: cannot found material with id: " + primaryKeys);
        setData(Materials.find(primaryKeys).getData());
    }

    @Override
    public int save() throws SQLException, ParseException {
        return Materials.save(this);
    }

    @Override
    public int delete() throws SQLException, ParseException {
        return Materials.delete(this);
    }

    @Override
    public HashMap<String, Object> getPrimaryKeys() {
        HashMap<String, Object> primaryKeys = new HashMap<>();
        for (SQLColumn sqlColumn : Materials.getSqlTable().getPrimaryKeys()) {
            primaryKeys.put(sqlColumn.getName(), data.get(sqlColumn.getName()));
        }
        return primaryKeys;
    }

    @Override
    public String toString() {
        return "Object-Material: " + data.toString();
    }
}
