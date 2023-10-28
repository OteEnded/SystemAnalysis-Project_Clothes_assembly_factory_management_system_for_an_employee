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
        data.put("material_id", material_id);
    }

    public String getName() {
        return (String) data.get("material_name");
    }

    public void setName(String material_name) {
        data.put("material_name", material_name);
    }

    public String getUnit() {
        return (String) data.get("unit_name");
    }

    public void setUnit(String unit_name) {
        data.put("unit_name", unit_name);
    }

    @Override
    public void load(int id) throws SQLException {
        load(EntityUtility.idFormatter(Materials.getSqlTable(), id));
    }

    @Override
    public void load(String id) throws SQLException {
        Materials.load();
        boolean isMaterialNew;
        try {
            isMaterialNew = Materials.isNew(id);
        }
        catch (RuntimeException e) {
            isMaterialNew = true;
        }
        if (isMaterialNew) throw new RuntimeException("Material[load]: Can't find material with material_id: " + id);
        setData(Materials.getData().get(id).getData());
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