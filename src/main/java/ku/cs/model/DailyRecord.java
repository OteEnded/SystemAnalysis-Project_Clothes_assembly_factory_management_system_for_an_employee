package ku.cs.model;

import ku.cs.entity.DailyRecords;
import ku.cs.entity.Products;
import ku.cs.entity.Works;
import ku.cs.utility.EntityUtility;

import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;

public class DailyRecord implements Row {

    private HashMap<String, Object> data = EntityUtility.getMap(DailyRecords.getSqlTable());

    public DailyRecord() {}

    public DailyRecord(HashMap<String, Object> data){
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

    public void setForWork(Work work) {
        setForWork(work.getId());
    }

    public void setForWork(String for_work) {
        if (!EntityUtility.isIdValid(Works.getSqlTable(), for_work)) throw new RuntimeException("DailyRecord[setForWork]: Invalid for_work -> " + for_work);
        data.put("for_work", for_work);
    }

    public String getForWorkId() {
        return (String) data.get("for_work");
    }

    public Work getForWork() throws SQLException {
        Work work = new Work();
        work.load((String) data.get("for_work"));
        return work;
    }

    public void setDate(Date date) {
        data.put("date", date);
    }

    public Date getDate() {
        return (Date) data.get("date");
    }

    public void setAmount(int amount) {
        data.put("amount", amount);
    }

    public int getAmount() {
        return (int) data.get("amount");
    }

    public void load(int work_id, Date date) throws SQLException {
        load(EntityUtility.idFormatter(Works.getSqlTable(), work_id), date);
    }

    public void load(Work work, Date date) throws SQLException {
        load(work.getId(), date);
    }


    public void load(String work_id, Date date) throws SQLException {
        load(String.join("|", work_id, date.toString()));
    }

    @Override
    public void load(String primaryKeys) throws SQLException {
        if (DailyRecords.isNew(this)) throw new RuntimeException("DailyRecord[load]: cannot found DailyRecord with primaryKeys -> " + primaryKeys);
        setData(DailyRecords.find(primaryKeys).getData());
    }

    @Override
    public int save() throws SQLException, ParseException {
        return DailyRecords.save(this);
    }

    @Override
    public int delete() throws SQLException, ParseException {
        return DailyRecords.delete(this);
    }

    @Override
    public HashMap<String, Object> getPrimaryKeys() {
        HashMap<String, Object> primaryKeys = new HashMap<>();
        for (SQLColumn sqlColumn : DailyRecords.getSqlTable().getPrimaryKeys()) {
            primaryKeys.put(sqlColumn.getName(), data.get(sqlColumn.getName()));
        }
        return primaryKeys;
    }

    @Override
    public String toString() {
        return "Object-DailyRecord: " + data.toString();
    }
}
