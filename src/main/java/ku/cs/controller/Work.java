package ku.cs.controller;

import java.time.LocalDate;

/* for test only */

public class Work {
    private String type;
    private String product;
    private Integer quantity;
    private LocalDate deadline;
    private String status;

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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
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

    public Work(String type, String product, Integer quantity, LocalDate deadline, String status) {
        this.type = type;
        this.product = product;
        this.quantity = quantity;
        this.deadline = deadline;
        this.status = status;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Work{");
        sb.append("type='").append(type).append('\'');
        sb.append(", product='").append(product).append('\'');
        sb.append(", quantity=").append(quantity);
        sb.append(", deadline=").append(deadline);
        sb.append(", status='").append(status).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
