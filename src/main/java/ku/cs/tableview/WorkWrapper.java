package ku.cs.tableview;

import java.time.LocalDate;
import java.util.Date;
import java.util.StringJoiner;

/* for test only */

public class WorkWrapper {
    private String id;
    private String type;
    private String product;
    private int quantity;
    private Date deadline;
    private String status;
    private int capacity;
    private String note;

    public WorkWrapper(String id, String type, String product, int quantity, Date deadline, String status, int capacity, String note) {
        this.id = id;
        this.type = type;
        this.product = product;
        this.quantity = quantity;
        this.deadline = deadline;
        this.status = status;
        this.capacity = capacity;
        this.note = note;
    }

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

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
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
}
