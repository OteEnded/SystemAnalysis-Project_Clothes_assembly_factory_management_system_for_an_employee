package ku.cs.entity;

import ku.cs.model.DailyRecord;
import ku.cs.model.SQLColumn;
import ku.cs.model.SQLRow;
import ku.cs.model.SQLTable;
import ku.cs.service.DataSourceDB;
import ku.cs.utility.ProjectUtility;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

public class DailyRecords {

    private static final SQLTable sqlTable = new SQLTable("DailyRecords");
    static {
        SQLColumn sqlColumn;

        sqlColumn = new SQLColumn();
        sqlColumn.setName("for_work");
        sqlColumn.setType("varchar(255)");
        sqlColumn.setPrimaryKey();
        sqlColumn.setForeignKey();
        sqlColumn.setForeignKeyTable("Works");
        sqlColumn.setForeignKeyColumn("work_id");
        sqlColumn.setOnDelete("CASCADE");
        sqlTable.addColumObj(sqlColumn);

        sqlColumn = new SQLColumn();
        sqlColumn.setName("date");
        sqlColumn.setType("date");
        sqlColumn.setNotNull();
        sqlColumn.setPrimaryKey();
        sqlTable.addColumObj(sqlColumn);

        sqlColumn = new SQLColumn();
        sqlColumn.setName("amount");
        sqlColumn.setType("int");
        sqlColumn.setNotNull();
        sqlTable.addColumObj(sqlColumn);
    }

    public static SQLTable getSqlTable() {
        return sqlTable;
    }

    private static HashMap<String, DailyRecord> data;
    static {
        data = new HashMap<>();
    }

    public static HashMap<String, DailyRecord> getData() {
        return data;
    }

    public static void setData(HashMap<String, DailyRecord> data) {
        DailyRecords.data = data;
    }

    public static HashMap<String, DailyRecord> load() throws SQLException {
        return load(true);
    }

    public static HashMap<String, DailyRecord> load(boolean updateBuffer) throws SQLException {
        HashMap<String, DailyRecord> dataFromDB = new HashMap<>();
        List<SQLRow> sqlRows = DataSourceDB.load(sqlTable);
        for (SQLRow sqlRow : sqlRows) {
            dataFromDB.put(sqlRow.getJoinedPrimaryKeys(), new DailyRecord(sqlRow.getValuesMap()));
        }
        if (updateBuffer) data = dataFromDB;
        return dataFromDB;
    }

    public static String getJoinedPrimaryKeys(DailyRecord dailyRecord) {
        SQLRow sqlRow = new SQLRow(sqlTable, dailyRecord);
        return sqlRow.getJoinedPrimaryKeys();
    }

    public static void addData(DailyRecord dailyRecord) {
        ProjectUtility.debug("DailyRecords[addData]: adding dailyRecord ->", dailyRecord);
        data.put(getJoinedPrimaryKeys(dailyRecord), dailyRecord);
        ProjectUtility.debug("DailyRecords[addData]: added dailyRecord with primaryKeys ->", getJoinedPrimaryKeys(dailyRecord), "=", dailyRecord);
    }

    public static boolean isNew(DailyRecord dailyRecord) throws SQLException {
        return isNew(getJoinedPrimaryKeys(dailyRecord));
    }

    public static boolean isNew(String primaryKeys) throws SQLException {
        if (data == null) load();
        if (data.isEmpty()) return true;
        return !data.containsKey(primaryKeys);
    }

    public static int save(DailyRecord dailyRecord) throws SQLException, ParseException {
        ProjectUtility.debug("DailyRecords[save]: saving dailyRecord ->", dailyRecord);
        if(isNew(dailyRecord)) {
            addData(dailyRecord);
            return DataSourceDB.exePrepare(sqlTable.getInsertQuery(new SQLRow(sqlTable, dailyRecord)));
        }
        data.put(getJoinedPrimaryKeys(dailyRecord), dailyRecord);
        return DataSourceDB.exePrepare(sqlTable.getUpdateQuery(new SQLRow(sqlTable, dailyRecord)));
    }

    public static int delete(DailyRecord dailyRecord) throws SQLException, ParseException {
        ProjectUtility.debug("DailyRecords[delete]: deleting dailyRecord ->", dailyRecord);
        if (isNew(dailyRecord)) throw new RuntimeException("DailyRecords[delete]: Can't delete dailyRecord that is not in database");
        data.remove(getJoinedPrimaryKeys(dailyRecord));
        return DataSourceDB.exePrepare(sqlTable.getDeleteQuery(new SQLRow(sqlTable, dailyRecord)));
    }

    public static HashMap<String, Object> filter;
    public static void addFilter(String column, Object value) {
        if (filter == null) filter = new HashMap<>();
        filter.put(column, value);
    }
    public static void setFilter(HashMap<String, Object> filter) {
        DailyRecords.filter = filter;
    }
    public static HashMap<String, Object> getFilter() {
        return filter;
    }
    public static HashMap<String, DailyRecord> getFilteredData(HashMap<String, Object> filter) throws SQLException {
        setFilter(filter);
        return getFilteredData();
    }
    public static HashMap<String, DailyRecord> getFilteredData() throws SQLException {
        if (filter == null) throw new RuntimeException("DailyRecords[getFilteredData]: filter is null, Please set filter or get all data without filter using -> DailyRecords.getData()");
        if (data == null) load();
        HashMap<String, DailyRecord> filteredData = new HashMap<>();
        for (DailyRecord dailyRecord : data.values()) {
            boolean isFiltered = true;
            for (String column : filter.keySet()) {
                if (!dailyRecord.getData().get(column).equals(filter.get(column))) {
                    isFiltered = false;
                    break;
                }
            }
            if (isFiltered) filteredData.put(getJoinedPrimaryKeys(dailyRecord), dailyRecord);
        }
        filter = null;
        return filteredData;
    }
}
