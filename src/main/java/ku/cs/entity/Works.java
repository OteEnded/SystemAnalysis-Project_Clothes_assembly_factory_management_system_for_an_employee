package ku.cs.entity;

import ku.cs.model.SQLColumn;
import ku.cs.model.SQLRow;
import ku.cs.model.SQLTable;
import ku.cs.model.Work;
import ku.cs.service.DataSourceDB;
import ku.cs.utility.EntityUtility;
import ku.cs.utility.ProjectUtility;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Works {

    public static String type_normal = "งานธรรมดา";
    public static String type_rush = "งานรีบ";
    public static String type_repair = "งานแก้";

    public static String status_waitForAccept = "รอรับงาน";
    public static String status_waitForMaterial = "รอวัตถุดิบ";
    public static String status_working = "กำลังทำงาน";
    public static String status_done = "ทำงานเสร็จ";
    public static String status_sent = "ส่งงานแล้ว";
    public static String status_checked = "ตรวจแล้ว";
    public static List<String> typeList = new ArrayList<>();
    static {
        typeList.add(type_normal);
        typeList.add(type_rush);
        typeList.add(type_repair);
    }

    public static List<String> statusList = new ArrayList<>();
    static {
        statusList.add(status_waitForAccept);
        statusList.add(status_waitForMaterial);
        statusList.add(status_working);
        statusList.add(status_done);
        statusList.add(status_sent);
        statusList.add(status_checked);
    }

    private static final SQLTable sqlTable = new SQLTable("Works");
    static {
        SQLColumn sqlColumn;

        sqlColumn = new SQLColumn();
        sqlColumn.setName("work_id");
        sqlColumn.setType("varchar(255)");
        sqlColumn.setPrimaryKey();
        sqlTable.addColumObj(sqlColumn);

        sqlColumn = new SQLColumn();
        sqlColumn.setName("work_type");
        sqlColumn.setType("varchar(255)");
        sqlColumn.setNotNull();
        sqlTable.addColumObj(sqlColumn);

        sqlColumn = new SQLColumn();
        sqlColumn.setName("product");
        sqlColumn.setType("varchar(255)");
        sqlColumn.setForeignKey();
        sqlColumn.setForeignKeyTable("Products");
        sqlColumn.setForeignKeyColumn("product_id");
        sqlColumn.setOnDelete("SET NULL");
        sqlTable.addColumObj(sqlColumn);

        sqlColumn = new SQLColumn();
        sqlColumn.setName("deadline");
        sqlColumn.setType("date");
        sqlColumn.setNotNull();
        sqlTable.addColumObj(sqlColumn);

        sqlColumn = new SQLColumn();
        sqlColumn.setName("create_date");
        sqlColumn.setType("date");
        sqlColumn.setNotNull();
        sqlTable.addColumObj(sqlColumn);

        sqlColumn = new SQLColumn();
        sqlColumn.setName("start_date");
        sqlColumn.setType("date");
        sqlTable.addColumObj(sqlColumn);

        sqlColumn = new SQLColumn();
        sqlColumn.setName("status");
        sqlColumn.setType("varchar(255)");
        sqlColumn.setDefaultValue("'รอรับงาน'");
        sqlColumn.setNotNull();
        sqlTable.addColumObj(sqlColumn);

        sqlColumn = new SQLColumn();
        sqlColumn.setName("goal_amount");
        sqlColumn.setType("int");
        sqlColumn.setNotNull();
        sqlTable.addColumObj(sqlColumn);

        sqlColumn = new SQLColumn();
        sqlColumn.setName("progress_amount");
        sqlColumn.setType("int");
        sqlColumn.setDefaultValue("0");
        sqlColumn.setNotNull();
        sqlTable.addColumObj(sqlColumn);

        sqlColumn = new SQLColumn();
        sqlColumn.setName("note");
        sqlColumn.setType("varchar(255)");
        sqlTable.addColumObj(sqlColumn);

        sqlColumn = new SQLColumn();
        sqlColumn.setName("repair_work");
        sqlColumn.setType("varchar(255)");
        sqlColumn.setForeignKey();
        sqlColumn.setForeignKeyTable("Works");
        sqlColumn.setForeignKeyColumn("work_id");
        sqlColumn.setOnDelete("CASCADE");
        sqlTable.addColumObj(sqlColumn);
    }

    public static SQLTable getSqlTable() {
        return sqlTable;
    }

    private static HashMap<String, Work> data;
    static {
        data = new HashMap<>();
    }

    public static HashMap<String, Work> getData() {
        return data;
    }

    public static void setData(HashMap<String, Work> data) {
        Works.data = data;
    }

    public static HashMap<String, Work> load() throws SQLException {
        return load(true);
    }

    public static HashMap<String, Work> load(boolean updateBuffer) throws SQLException {
        HashMap<String, Work> dataFromDB = new HashMap<>();
        List<SQLRow> sqlRows = DataSourceDB.load(sqlTable);
        for (SQLRow sqlRow: sqlRows) {
            dataFromDB.put(sqlRow.getJoinedPrimaryKeys(), new Work(sqlRow.getValuesMap()));
        }
        if (updateBuffer) data = dataFromDB;
        return dataFromDB;
    }

    public static String getJoinedPrimaryKeys(Work work) {
        SQLRow sqlRow = new SQLRow(sqlTable, work);
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

    public static void addData(Work work) throws SQLException {
        ProjectUtility.debug("Works[addData]: adding work ->", work);
        if (data == null) data = new HashMap<>();
        if (work.getId() == null) work.setId(getNewId());
        data.put(getJoinedPrimaryKeys(work), work);
        ProjectUtility.debug("Works[addData]: added work with primaryKeys ->", getJoinedPrimaryKeys(work), "=", work);
    }

    public static boolean isNew(Work work) throws SQLException {
        return isNew(work.getId());
    }

    public static boolean isNew(String primaryKeys) throws SQLException {
        if (data == null) load();
        if (data.isEmpty()) return true;
        return !data.containsKey(primaryKeys);
    }

    public static int save(Work work) throws SQLException, ParseException {
        ProjectUtility.debug("Works[save]: saving work ->", work);
        if (isNew(work)){
            addData(work);
            return DataSourceDB.exePrepare(sqlTable.getInsertQuery(new SQLRow(sqlTable, work)));
        }
        data.put(getJoinedPrimaryKeys(work), work);
        return DataSourceDB.exePrepare(sqlTable.getUpdateQuery(new SQLRow(sqlTable, work)));
    }

    public static int delete(String id) throws SQLException, ParseException {
        Work work = new Work();
        work.load(id);
        return delete(work);
    }

    public static int delete(Work work) throws SQLException, ParseException {
        ProjectUtility.debug("Works[delete]: deleting work ->", work);
        if (isNew(work)) throw new RuntimeException("Works[delete]: Can't find work with work_id: " + work.getId());
        data.remove(getJoinedPrimaryKeys(work));
        return DataSourceDB.exePrepare(sqlTable.getDeleteQuery(new SQLRow(sqlTable, work)));
    }

    public static HashMap<String, Object> filter;
    public static void addFilter(String column, Object value) {
        if (filter == null) filter = new HashMap<>();
        filter.put(column, value);
    }
    public static void setFilter(HashMap<String, Object> filter) {
        Works.filter = filter;
    }
    public static HashMap<String, Object> getFilter() {
        return filter;
    }

    public static HashMap<String, Work> getFilteredData(HashMap<String, Object> filter) throws SQLException {
        setFilter(filter);
        return getFilteredData();
    }
    public static HashMap<String, Work> getFilteredData() throws SQLException {
        if (filter == null) throw new RuntimeException("Works[getFilteredData]: filter is null. Please set filter first or get all data without filter using -> Works.getdata()");
        if (data == null) load();
        HashMap<String, Work> filteredData = new HashMap<>();
        for (Work work: getData().values()) {
            boolean isFiltered = true;
            for (String column: filter.keySet()) {
                if (!work.getData().get(column).equals(filter.get(column))) {
                    isFiltered = false;
                    break;
                }
            }
            if (isFiltered) filteredData.put(work.getId(), work);
        }
        filter = null;
        return filteredData;
    }
}
