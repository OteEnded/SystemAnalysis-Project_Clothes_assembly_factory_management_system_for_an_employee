package ku.cs.controller;

import java.time.LocalDate;
import java.util.StringJoiner;

/* for test only */

public class Work {
    private String type;
    private String product;
    private int quantity;
    private LocalDate deadline;
    private String status;
    private int capacity;
    private String note;

    public Work(String type, String product, int quantity, LocalDate deadline, String status, int capacity, String note) {
        this.type = type;
        this.product = product;
        this.quantity = quantity;
        this.deadline = deadline;
        this.status = status;
        this.capacity = capacity;
        this.note = note;
    }

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

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Work.class.getSimpleName() + "[", "]")
                .add("type='" + type + "'")
                .add("product='" + product + "'")
                .add("quantity=" + quantity)
                .add("deadline=" + deadline)
                .add("status='" + status + "'")
                .add("capacity=" + capacity)
                .add("note=" + note)
                .toString();
    }
}
