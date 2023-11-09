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
//        Works.addFilter("repair_work", getId());
//        ProjectUtility.debug(repairWorks);
        return true;
    }

    public int getRecommendedProgressRate() throws SQLException, ParseException {
//        double bufferProgressRate = getProduct().getProgressRate();
//        int recommendedProgressRate = (int) bufferProgressRate;
//        while (true){
//            Product product = getProduct();
//            product.setProgressRate(recommendedProgressRate);
//            product.save();
//            ProjectUtility.debug(product);
//            if (getEstimated().equals(Works.estimate_onTime)) break;
//            recommendedProgressRate += 1;
//        }
//
//        Product product = getProduct();
//        product.setProgressRate(bufferProgressRate);
//        product.save();
        int recommendedProgressRate = (int) ProjectUtility.getDate().toLocalDate().toEpochDay() * ProjectUtility.differanceDate(getDeadline(), ProjectUtility.getDate());
        if (recommendedProgressRate < 0) recommendedProgressRate *= -1;
        recommendedProgressRate = recommendedProgressRate % 12;
        if (recommendedProgressRate < 5) recommendedProgressRate = 7;

        return recommendedProgressRate;
    }

    public int getRecommendedGoalAmount() throws SQLException {
        int bufferGoalAmount = getGoalAmount();
        int recommendedGoalAmount = bufferGoalAmount;
        while (recommendedGoalAmount >= 0){
            ProjectUtility.debug("Work[getRecommendedGoalAmount]: tring ->", recommendedGoalAmount);
            setGoalAmount(recommendedGoalAmount);
            if (getEstimated().equals(Works.estimate_onTime)) break;
            recommendedGoalAmount -= 1;
        }

        setGoalAmount(bufferGoalAmount);
        return recommendedGoalAmount;
    }

    public Date getRecommendedDeadline() throws SQLException {
        Date bufferDeadline = ProjectUtility.getDate(ProjectUtility.differanceDate(ProjectUtility.getDate(), getDeadline()));
        Date recommendedDeadline = ProjectUtility.getDate(ProjectUtility.differanceDate(ProjectUtility.getDate(), getDeadline()));
        while (true){
            setDeadline(recommendedDeadline);
            if (getEstimated().equals(Works.estimate_onTime)) break;
            recommendedDeadline = ProjectUtility.getDateWithOffset(recommendedDeadline, 1);
        }

        setDeadline(bufferDeadline);
        return recommendedDeadline;
    }

    public int getEstimatedWorkDay() throws SQLException {
        return (int) Math.ceil(getRemainingAmount() / getProduct().getProgressRate());
    }

    public boolean isRecorded(Date date) throws SQLException {
        return DailyRecords.isRecorded(this, date);
    }

    public String getAdviceMessage(){
        return "";
    }



    public void load(int id) throws SQLException {
        load(EntityUtility.idFormatter(Works.getSqlTable(), id));
    }

    @Override
    public void load(String primaryKeys) throws SQLException {
        boolean cannotLoad;
        try {
            cannotLoad = Works.isNew(primaryKeys);
        } catch (RuntimeException e) {
            cannotLoad = true;
        }
        cannotLoad = cannotLoad || !EntityUtility.isIdValid(Works.getSqlTable(), primaryKeys);
        if (cannotLoad) throw new RuntimeException("Work[getAll]: Can't getAll work with primaryKeys: " + primaryKeys);
        setData(Works.getData().get(primaryKeys).getData());
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
