package ku.cs.model;

import ku.cs.entity.DailyRecords;
import ku.cs.entity.Works;
import ku.cs.service.WorkCalendar;
import ku.cs.utility.EntityUtility;
import ku.cs.utility.ProjectUtility;

import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Work implements Row {

    private HashMap<String, Object> data = EntityUtility.getMap(Works.getSqlTable());

    public Work(){
        setProgressAmount(0);
        setCreateDate(ProjectUtility.getDate());
        setStatus(Works.status_waitForAccept);
    }

    public Work(HashMap<String, Object> data){
        setData(data);
    }

    @Override
    public void setData(HashMap<String, Object> data){ this.data = data; }

    @Override
    public HashMap<String, Object> getData() {
        return data;
    }

    public void setId(String work_id) {
        if (! EntityUtility.isIdValid(Works.getSqlTable(), work_id)) throw new RuntimeException("Work[setId]: Invalid work_id -> " + work_id);
        data.put("work_id", work_id);
    }

    public String getId() {
        return (String) data.get("work_id");
    }

    public void setWorkType(String work_type) {
        if (! Works.typeList.contains(work_type)) throw new RuntimeException("Work[setWorkType]: Invalid work_type-> " + work_type + " (not in " + List.of(Works.typeList) + ")");
        data.put("work_type", work_type);
    }

    public String getWorkType() {
        return (String) data.get("work_type");
    }

    public void setProduct(Product product) {
        setProduct(product.getId());
    }

    public void setProduct(String product) {
        data.put("product", product);
    }

    public String getProductId() {
        return (String) data.get("product");
    }

    public Product getProduct() throws SQLException {
        Product product = new Product();
        product.load((String) data.get("product"));
        return product;
    }

    public void setDeadline(Date deadline) {
        data.put("deadline", deadline);
    }

    public Date getDeadline() {
        return (Date) data.get("deadline");
    }

    public void setCreateDate(Date create_date) {
        data.put("create_date", create_date);
    }

    public Date getCreateDate() {
        return (Date) data.get("create_date");
    }

    public void setStartDate(Date start_date) {
        data.put("start_date", start_date);
    }

    public Date getStartDate() {
        return (Date) data.get("start_date");
    }

    public void setStatus(String status) {
        if (! Works.statusList.contains(status)) throw new RuntimeException("Work[setStatus]: Invalid status-> " + status + " (not in " + List.of(Works.statusList) + ")");
        data.put("status", status);
    }

    public String getStatus() {
        return (String) data.get("status");
    }

    public void setGoalAmount(int goal_amount) {
        data.put("goal_amount", goal_amount);
    }

    public int getGoalAmount() {
        if (data.get("goal_amount") == null) throw new RuntimeException("Work[getGoalAmount]: goal_amount is null");
        return (int) data.get("goal_amount");
    }

    public void setProgressAmount(int progress_amount) {
        data.put("progress_amount", progress_amount);
    }

    public int getProgressAmount() {
        if (data.get("progress_amount") == null) throw new RuntimeException("Work[getProgressAmount]: progress_amount is null");
        return (int) data.get("progress_amount");
    }

    public int getRemainingAmount() {
        return getGoalAmount() - getProgressAmount();
    }

    public void setNote (String note) {
        data.put("note", note);
    }

    public String getNote() {
        return (String) data.get("note");
    }


    // not done
    public String getEstimated() throws SQLException {
        return WorkCalendar.getWorkEstimating(this);
    }

    public boolean isPass() throws SQLException {
        return !getNote().contains(Works.note_haveToRepair);
    } 

    public int getRecommendedProgressRate() throws SQLException, ParseException {

        if (getId() == null) setId(Works.getNewId());

        double bufferProgressRate = getProduct().getProgressRate();
        Product product = getProduct();
        product.setProgressRate(1);
        product.save();

        WorkCalendar.init();
        HashMap<String, Object> event = WorkCalendar.getEventFromWork(this);
        if (event == null) throw new RuntimeException("Work[getRecommendedProgressRate]: event is null");
        ProjectUtility.debug("Work[getRecommendedProgressRate]: event ->", event);
        int avaliableDay = ProjectUtility.differanceDate (getDeadline(), (Date) event.get("start_date"));
        ProjectUtility.debug("Work[getRecommendedProgressRate]: avaliableDay ->", avaliableDay);
        if (avaliableDay <= 0) {
            product.setProgressRate(bufferProgressRate);
            product.save();
            return -1;
        }
        int recommendedProgressRate = (int) Math.ceil(getRemainingAmount() / avaliableDay);

        product.setProgressRate(bufferProgressRate);
        product.save();

        return recommendedProgressRate;
    }

    public int getRecommendedGoalAmount() throws SQLException {

        if (getId() == null) setId(Works.getNewId());

        HashMap<String, Work> workInPlan = new HashMap<>(Works.load());
        workInPlan.put(getId(), this);
        HashMap<String, Object> event = WorkCalendar.getEventFromWork(this, WorkCalendar.fetch(workInPlan));

        if (event == null) throw new RuntimeException("Work[getRecommendedDeadline]: event is null");
        ProjectUtility.debug("Work[getRecommendedDeadline]: event ->", event);

        int avaliableDay = ProjectUtility.differanceDate (getDeadline(), (Date) event.get("start_date"));
        ProjectUtility.debug("Work[getRecommendedProgressRate]: avaliableDay ->", avaliableDay);

        return (int) getProduct().getProgressRate() / avaliableDay;
    }

    public Date getRecommendedDeadline() throws SQLException {

        if (getId() == null) setId(Works.getNewId());

        HashMap<String, Work> workInPlan = new HashMap<>(Works.load());
        workInPlan.put(getId(), this);
        HashMap<String, Object> event = WorkCalendar.getEventFromWork(this, WorkCalendar.fetch(workInPlan));

        if (event == null) throw new RuntimeException("Work[getRecommendedDeadline]: event is null");
        ProjectUtility.debug("Work[getRecommendedDeadline]: event ->", event);
        int workDay = (int) Math.ceil(getGoalAmount() * getProduct().getProgressRate());

        return ProjectUtility.getDateWithOffset((Date) event.get("start_date"), workDay);
    }

    public int getEstimatedWorkDay() throws SQLException {
        return (int) Math.ceil(getRemainingAmount() / getProduct().getProgressRate());
    }

    public boolean isRecorded(Date date) throws SQLException {
        return DailyRecords.isRecorded(this, date);
    }

    public void load(int id) throws SQLException {
        load(EntityUtility.idFormatter(Works.getSqlTable(), id));
    }

    @Override
    public void load(String primaryKeys) throws SQLException {
        EntityUtility.checkId(Works.getSqlTable(), primaryKeys);
        setId(primaryKeys);
        if (Works.isNew(this)) throw new RuntimeException("Work[load]: cannot found work with id -> " + primaryKeys);
        setData(Works.find(primaryKeys).getData());
    }

    @Override
    public int save() throws SQLException, ParseException {
        return Works.save(this);
    }

    @Override
    public int delete() throws SQLException, ParseException {
        return Works.delete(this);
    }

    @Override
    public HashMap<String, Object> getPrimaryKeys() {
        HashMap<String, Object> primaryKeys = new HashMap<>();
        for (SQLColumn sqlColumn: Works.getSqlTable().getPrimaryKeys()) {
            primaryKeys.put(sqlColumn.getName(), data.get(sqlColumn.getName()));
        }
        return primaryKeys;
    }

    @Override
    public String toString() {
        return "Object-Work: " + data.toString();
    }
}
