package be.rayanhaddou.equipmentrental.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class AddToCartRequest {

    @NotNull
    private Long productId;

    @Min(1)
    private int quantity;

    @NotNull
    private LocalDate fromDate;

    @NotNull
    private LocalDate toDate;

    public Long getProductId() { return productId; }
    public int getQuantity() { return quantity; }
    public LocalDate getFromDate() { return fromDate; }
    public LocalDate getToDate() { return toDate; }

    public void setProductId(Long productId) { this.productId = productId; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setFromDate(LocalDate fromDate) { this.fromDate = fromDate; }
    public void setToDate(LocalDate toDate) { this.toDate = toDate; }
}
