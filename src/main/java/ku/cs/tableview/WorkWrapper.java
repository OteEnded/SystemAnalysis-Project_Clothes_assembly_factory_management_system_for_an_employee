package ku.cs.tableview;

import ku.cs.model.Product;
import ku.cs.model.Work;

import java.sql.SQLException;
import java.time.LocalDate;

/* for tableView only */

public class WorkWrapper {

    private String estimate;
    private String id;
    private String type;
    private LocalDate deadline;
    private String status;
    private int goal_amount;
    private int progress_amount;
    private String note;
    private Work repair_work;

    private String display_product;
    private String isPass;


    public WorkWrapper(Work work) throws SQLException {
        this.id = work.getId();
        this.type = work.getWorkType();
        this.deadline = work.getDeadline().toLocalDate();
        this.status = work.getStatus();
        this.goal_amount = work.getGoalAmount();
        this.progress_amount = work.getProgressAmount();
        this.note = work.getNote();
        this.repair_work = work.isPass() ? getRepair_work() : null;
        this.estimate = work.getEstimated();
        Product product = work.getProduct();
        this.display_product = product.getName() + " ขนาด " + product.getSize() + " นิ้ว";
        this.isPass = work.isPass() ? "ผ่าน" : "ไม่ผ่าน";
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public String getStatus() {
        return status;
    }

    public int getGoal_amount() {
        return goal_amount;
    }

    public int getProgress_amount() {
        return progress_amount;
    }

    public String getNote() {
        return note;
    }

    public Work getRepair_work() {
        return repair_work;
    }

    public String getDisplay_product() {
        return display_product;
    }

    public String getIsPass() {
        return isPass;
    }

    public String getEstimate() {
        return estimate;
    }
}
