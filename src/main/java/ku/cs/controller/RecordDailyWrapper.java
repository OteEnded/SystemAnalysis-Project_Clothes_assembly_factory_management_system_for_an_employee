package ku.cs.controller;

import javafx.scene.control.TextField;
import org.w3c.dom.Text;

import java.time.LocalDate;

public class RecordDailyWrapper {
    private String type;
    private String product;
    private int quantity;
    private int capacity;
    private LocalDate deadline;
    private TextField dailyRecord;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public TextField getDailyRecord() {
        return dailyRecord;
    }

    public void setDailyRecord(TextField dailyRecord) {
        this.dailyRecord = dailyRecord;
    }

    public RecordDailyWrapper(String type, String product, int quantity, int capacity, LocalDate deadline, TextField dailyRecord) {
        this.type = type;
        this.product = product;
        this.quantity = quantity;
        this.capacity = capacity;
        this.deadline = deadline;
        this.dailyRecord = dailyRecord;
    }
}
