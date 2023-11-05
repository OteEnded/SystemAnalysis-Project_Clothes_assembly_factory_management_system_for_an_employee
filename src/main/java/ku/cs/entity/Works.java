package ku.cs.entity;

import ku.cs.model.*;
import ku.cs.service.DataSourceDB;
import ku.cs.service.WorkCalendar;
import ku.cs.utility.EntityUtility;
import ku.cs.utility.PopUpUtility;
import ku.cs.utility.ProjectUtility;

import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;

public class Works {

    public static final String type_normal = "งานธรรมดา";
    public static final String type_rush = "งานรีบ";
    public static final String type_repair = "งานแก้";

    public static final String status_waitForAccept = "รอรับงาน";
    public static final String status_waitForMaterial = "รอวัตถุดิบ";
    public static final String status_waitForWorking = "รอทำงาน";
    public static final String status_working = "กำลังทำงาน";
    public static final String status_done = "ทำงานเสร็จ";
    public static final String status_sent = "ส่งงานแล้ว";
    public static final String status_checked = "ตรวจแล้ว";

    public static final String estimate_onTime = "ทันเวลา";
    public static final String estimate_late = "อาจไม่ทันเวลา";

    public static final String note_waitForUserEstimate = "รอลูกจ้างประเมินเวลา";
    public static final String note_userEstimatedLate = "ลูกจ้างประเมินเวลาแล้ว อาจทำงานได้ไม่ทันเวลา";

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
        statusList.add(status_waitForWorking);
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
        sqlColumn.setOnDelete("CASCADE");
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
        sqlColumn.setDefaultValue(Works.status_waitForAccept);
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
        sqlColumn.setDefaultValue(0);
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

    public static HashMap<String, Work> getData() throws SQLException{
        if(data == null) load();
        return data;
    }

    public static List<Work> getDataAsList() throws SQLException {
        if(data == null) load();
        return toList(data);
    }

    public static List<Work> toList(HashMap<String, Work> data) {
        return new ArrayList<>(data.values());
    }

    public static void setData(HashMap<String, Work> data) {
        Works.data = data;
    }

    public static HashMap<String, Work> load() throws SQLException {
        return load(true);
    }

    public static HashMap<String, Work> load(boolean updateBuffer) throws SQLException {

        try {
            PopUpUtility.popUp("loading", "Works (งาน)");
        } catch (Exception e){
            ProjectUtility.debug("Works[load]: cannot do pop ups thing");
            ProjectUtility.debug(e);
//            e.printStackTrace();
        }

        HashMap<String, Work> dataFromDB = new HashMap<>();
        List<SQLRow> sqlRows = DataSourceDB.load(sqlTable);
        for (SQLRow sqlRow: sqlRows) {
            dataFromDB.put(sqlRow.getJoinedPrimaryKeys(), new Work(sqlRow.getValuesMap()));
        }
        if (updateBuffer) data = dataFromDB;

        try {
            PopUpUtility.close("loading", true);
        } catch (Exception e){
            ProjectUtility.debug("Works[load]: cannot do pop ups thing");
            ProjectUtility.debug(e);
        }

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
        if (data == null) load();
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

    public static boolean isWorkValid(Work work) {
        return verifyWork(work).isEmpty();
    }

    public static List<String> verifyWork(Work work) {
        List<String> error = new ArrayList<>(EntityUtility.verifyRowByTable(sqlTable, work));
        return error;
    }

    public static int save(Work work) throws SQLException, ParseException {
        ProjectUtility.debug("Works[save]: saving work ->", work);

        if (!isWorkValid(work)) throw new RuntimeException("Works[save]: work's data is not valid -> " + verifyWork(work));

        int affectedRow = 0;
        if (isNew(work)){
            Products.load();
            if (work.getProduct().getProgressRate() == -1){
                work.setNote(Works.note_waitForUserEstimate + "\n" + work.getNote());
            }
            addData(work);
            if (isNew(work)) return save(work);
            affectedRow += DataSourceDB.exePrepare(sqlTable.getInsertQuery(new SQLRow(sqlTable, work)));
            load();
            WorkCalendar.init();
            return affectedRow;
        }
        data.put(getJoinedPrimaryKeys(work), work);
        affectedRow += DataSourceDB.exePrepare(sqlTable.getUpdateQuery(new SQLRow(sqlTable, work)));
        load();
        WorkCalendar.init();
        return affectedRow;
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
        int affectedRow = DataSourceDB.exePrepare(sqlTable.getDeleteQuery(new SQLRow(sqlTable, work)));
        Works.load();
        DailyRecords.load();
        return affectedRow;
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
        if (filter == null) throw new RuntimeException("Works[getFilteredData]: filter is null. Please set filter first or get all data without filter using -> Works.getData()");
        if (data == null) load();
        HashMap<String, Work> filteredData = new HashMap<>();
        for (Work work: getData().values()) {
            boolean isFiltered = true;
            for (String column: filter.keySet()) {
                if (work.getData().get(column) == null) {
                    isFiltered = false;
                    break;
                }
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

    public static List<Work> getSortedBy(String column) throws SQLException {
        if (data == null) load();
        return getSortedBy(column, data);
    }

    public static List<Work> getSortedBy(String column, HashMap<String, Work> data) throws SQLException {
//        ProjectUtility.debug("Works[getSortedBy]: getting data sorted by ->", column);
//        if (data == null) throw new RuntimeException("Works[getSortedBy]: data is null. Please load data first or get all data without filter using -> Works.getData()");
//        List<String> sortedValues = new ArrayList<String>();
//        for (Work work : data.values()) {
//            sortedValues.add(work.getData().get(column).toString());
//        }
//        Collections.sort(sortedValues);
//        ProjectUtility.debug("Works[getSortedBy]: sorted target ->", sortedValues);
//        List<Work> sortedWorks = new ArrayList<>();
//        for (String sortedValue : sortedValues) {
//            addFilter(column, ProjectUtility.castStringToObject(sortedValue, sqlTable.getColumnByName(column).getClassType()));
//            sortedWorks.addAll(getFilteredData().values());
//        }
//        return sortedWorks;
        List<Work> works = toList(data);
        works.sort((o1, o2) -> {
            try {
                return o1.getData().get(column).toString().compareTo(o2.getData().get(column).toString());
            } catch (RuntimeException e) {
                return 0;
            }
        });
        return works;
    }

    public static HashMap<String, Work> getAbnormalWorks() throws SQLException {
        if (data == null) load();
        HashMap<String, Work> abnormalWorks = new HashMap<>();
        for (Work work: getData().values()) {
            if (Objects.equals(work.getEstimated(), Works.estimate_late)) abnormalWorks.put(work.getId(), work);
        }

        for (Work work: abnormalWorks.values()) {
            if (work.getStatus().equals(Works.status_done) || work.getStatus().equals(Works.status_sent) || work.getStatus().equals(Works.status_checked)) {
                abnormalWorks.remove(getJoinedPrimaryKeys(work));
            }
        }

        return abnormalWorks;
    }

    public static HashMap<String, Work> getWorkWaitingForDairyRecord(Date date) throws SQLException {
        if (data == null) load();
        HashMap<String, Work> workWaitingForDairyRecord = new HashMap<>();
        for (Work work: getData().values()) {
            if (work.getStatus().equals(Works.status_working) && !work.isRecorded(date)) workWaitingForDairyRecord.put(work.getId(), work);
        }
        return workWaitingForDairyRecord;
    }
}
