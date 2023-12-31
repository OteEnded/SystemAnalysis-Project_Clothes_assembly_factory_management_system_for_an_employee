package ku.cs.model;

import ku.cs.utility.ProjectUtility;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

// อันนี้เป็นคราสที่ระบุคอลัมน์ของ SQLTable แต่ละคอลัมน์จะมีค่าต่างๆ ที่ระบุไว้ในนี้
public class SQLColumn {
    private String name;
    private Class<?> classType;
    private String type;
    private boolean isPrimaryKey = false;
    private boolean isForeignKey = false;
    private boolean isUnique = false;
    private boolean isNotNull = false;
    private Object defaultValue;
    private String foreignKeyTable;
    private String foreignKeyColumn;
    private String onDelete;
    private String onUpdate;

    public static Map<Class<?>, String> typeMap = new HashMap<>();
    static {
        typeMap.put(String.class, "varchar(255)");
        typeMap.put(Integer.class, "int");
        typeMap.put(int.class, "int");
        typeMap.put(Long.class, "bigint");
        typeMap.put(long.class, "bigint");
        typeMap.put(Float.class, "float");
        typeMap.put(float.class, "float");
        typeMap.put(Double.class, "double");
        typeMap.put(double.class, "double");
        typeMap.put(Boolean.class, "boolean");
        typeMap.put(boolean.class, "boolean");
        typeMap.put(Date.class, "date");
        typeMap.put(Timestamp.class, "Timestamp");
    }

    public static Map<String, Class<?>> classTypeMap = new HashMap<>();
    static {
        classTypeMap.put("varchar(255)", String.class);
        classTypeMap.put("int", Integer.class);
        classTypeMap.put("bigint", Long.class);
        classTypeMap.put("float", Float.class);
        classTypeMap.put("double", Double.class);
        classTypeMap.put("boolean", Boolean.class);
        classTypeMap.put("date", Date.class);
        classTypeMap.put("Timestamp", Timestamp.class);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class<?> getClassType() {
        return classType;
    }

    public void setClassType(Object obj) {
        this.setClassType(obj.getClass());
    }

    public void setClassType(Class<?> classType) {
        this.classType = classType;
        this.type = typeMap.get(classType);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
        this.classType = classTypeMap.get(type);
    }

    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    public void setPrimaryKey() {
        setPrimaryKey(true);
    }

    public void setPrimaryKey(boolean primaryKey) {
        isPrimaryKey = primaryKey;
    }

    public void setForeignKey() {
        setForeignKey(true);
    }

    public boolean isForeignKey() {
        return isForeignKey;
    }

    public void setForeignKey(boolean foreignKey) {
        isForeignKey = foreignKey;
    }

    public boolean isUnique() {
        return isUnique;
    }

    public void setUnique() {
        setUnique(true);
    }

    public void setUnique(boolean unique) {
        isUnique = unique;
    }

    public boolean isNotNull() {
        return isNotNull;
    }

    public void setNotNull() {
        setNotNull(true);
    }

    public void setNotNull(boolean notNull) {
        isNotNull = notNull;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        setDefaultValue(ProjectUtility.castStringToObject(defaultValue, classType));
    }

    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getForeignKeyTable() {
        return foreignKeyTable;
    }

    public void setForeignKeyTable(String foreignKeyTable) {
        this.foreignKeyTable = foreignKeyTable;
    }

    public String getForeignKeyColumn() {
        return foreignKeyColumn;
    }

    public void setForeignKeyColumn(String foreignKeyColumn) {
        this.foreignKeyColumn = foreignKeyColumn;
    }

    public String getOnDelete() {
        return onDelete;
    }

    public void setOnDelete(String onDelete) {
        this.onDelete = onDelete;
    }

    public String getOnUpdate() {
        return onUpdate;
    }

    public void setOnUpdate(String onUpdate) {
        this.onUpdate = onUpdate;
    }

    public String getCreateColum(){
        String sql = name + " " + type + " ";
        if (defaultValue != null) {
            String formattedDefaultValue = defaultValue.toString();
            if (classType.equals(String.class)) formattedDefaultValue = "'" + formattedDefaultValue + "'";
            sql += "DEFAULT " + formattedDefaultValue + " ";
        }
        if (isPrimaryKey) {
            sql += "PRIMARY KEY ";
            return sql;
        }
        if (isNotNull) sql += "NOT NULL ";
        return sql;
    }

    public String getCreateForeignKey(){
        String sql = "FOREIGN KEY (" + name + ") REFERENCES " + foreignKeyTable + "(" + foreignKeyColumn + ") ";
        if (onDelete != null && !onDelete.equals("")) sql += "ON DELETE " + onDelete + " ";
        if (onUpdate != null && !onUpdate.equals("")) sql += "ON UPDATE " + onUpdate + " ";
        return sql;
    }

    @Override
    public String toString() {
        return "SQLColumn[name]: "+ name +" {" +
                "class_type=" + classType +
                ", type='" + type + '\'' +
                ", isPrimaryKey=" + isPrimaryKey +
                ", isForeignKey=" + isForeignKey +
                ", isUnique=" + isUnique +
                ", isNotNull=" + isNotNull +
                ", defaultValue='" + defaultValue + '\'' +
                ", foreignKeyTable='" + foreignKeyTable + '\'' +
                ", foreignKeyColumn='" + foreignKeyColumn + '\'' +
                ", onDelete='" + onDelete + '\'' +
                ", onUpdate='" + onUpdate + '\'' +
                '}';
    }
}
