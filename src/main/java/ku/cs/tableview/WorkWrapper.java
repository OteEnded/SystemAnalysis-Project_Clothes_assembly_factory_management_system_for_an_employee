package ku.cs.tableview;

import java.time.LocalDate;

/* for tableView only */

public class WorkWrapper {

    //work
    private String id;
    private String type;
    private LocalDate deadline;
    private String status;
    private int goal_amount;
    private int progress_amount;
    private String note;
    private String repair_work;

    //product
    private String product_name;
    private int product_size;
    private int product_progress_rate;

    //for display

    //from model
    private boolean isPass;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getGoal_amount() {
        return goal_amount;
    }

    public void setGoal_amount(int goal_amount) {
        this.goal_amount = goal_amount;
    }

    public int getProgress_amount() {
        return progress_amount;
    }

    public void setProgress_amount(int progress_amount) {
        this.progress_amount = progress_amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getRepair_work() {
        return repair_work;
    }

    public void setRepair_work(String repair_work) {
        this.repair_work = repair_work;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public int getProduct_size() {
        return product_size;
    }

    public void setProduct_size(int product_size) {
        this.product_size = product_size;
    }

    public int getProduct_progress_rate() {
        return product_progress_rate;
    }

    public void setProduct_progress_rate(int product_progress_rate) {
        this.product_progress_rate = product_progress_rate;
    }

    public String getDisplay_product() {
        return display_product;
    }

    public String getChecked_work_status() {
        return checked_work_status;
    }
}
