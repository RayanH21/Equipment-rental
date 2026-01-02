package be.rayanhaddou.equipmentrental.model;

import java.time.LocalDate;

public class CartItem {

    private Long productId;
    private String productName;
    private int quantity;
    private LocalDate fromDate;
    private LocalDate toDate;

    public CartItem(Long productId, String productName, int quantity,
                    LocalDate fromDate, LocalDate toDate) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public Long getProductId() { return productId; }
    public String getProductName() { return productName; }
    public int getQuantity() { return quantity; }
    public LocalDate getFromDate() { return fromDate; }
    public LocalDate getToDate() { return toDate; }
}
