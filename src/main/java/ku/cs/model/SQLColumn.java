package ku.cs.model;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

// อันนี้เป็นคราสที่ระบุคอลัมน์ของ SQLTable แต่ละคอลัมน์จะมีค่าต่างๆ ที่ระบุไว้ในนี้
public class SQLColumn {
    private String name = "";
    private Class<?> classType = null;
    private String type = "";
    private boolean isPrimaryKey = false;
    private boolean isForeignKey = false;
    private boolean isUnique = false;
    private boolean isNotNull = false;
    private String defaultValue = "";
    private String foreignKeyTable = "";
    private String foreignKeyColumn = "";
    private String onDelete = "";
    private String onUpdate = "";

    public static Map<Class<?>, String> typeMap = new HashMap<>();
    static {
        typeMap.put(String.class, "varchar(255)");
        typeMap.put(Integer.class, "int");
        typeMap.put(Long.class, "bigint");
        typeMap.put(Float.class, "float");
        typeMap.put(Double.class, "double");
        typeMap.put(Boolean.class, "boolean");
        typeMap.put(Timestamp.class, "date");
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

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
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
        if (defaultValue != null && !defaultValue.equals("")) sql += "DEFAULT " + defaultValue + " ";
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
