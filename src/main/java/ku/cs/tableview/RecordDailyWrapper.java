package ku.cs.tableview;

import javafx.scene.control.TextField;
import ku.cs.model.Product;
import ku.cs.model.Work;
import org.w3c.dom.Text;

import java.sql.SQLException;
import java.time.LocalDate;

public class RecordDailyWrapper {

    private String id;
    private String type;
    private LocalDate deadline;
    private int goal_amount;
    private int progress_amount;
    private String display_product;
    private TextField dailyRecord;

    public RecordDailyWrapper(Work work, TextField dailyRecord) throws SQLException {
        this.id = work.getId();
        this.type = work.getWorkType();
        this.deadline = work.getDeadline().toLocalDate();
        this.goal_amount = work.getGoalAmount();
        this.progress_amount = work.getProgressAmount();
        Product product = work.getProduct();
        this.display_product = product.getName() + " ขนาด " + product.getSize() + " นิ้ว";
        this.dailyRecord = dailyRecord;
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

    public int getGoal_amount() {
        return goal_amount;
    }

    public int getProgress_amount() {
        return progress_amount;
    }

    public String getDisplay_product() {
        return display_product;
    }

    public TextField getDailyRecord() {
        return dailyRecord;
    }
}
