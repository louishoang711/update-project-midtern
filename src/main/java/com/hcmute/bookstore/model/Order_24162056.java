package com.hcmute.bookstore.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Order_24162056 {
    private static final DateTimeFormatter DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private int orderId;
    private int userId;
    private String receiverName;
    private String receiverPhone;
    private String shippingAddress;
    private BigDecimal totalAmount;
    private String paymentMethod;
    private String status;
    private LocalDateTime createdAt;
    private List<OrderItem_24162056> items = new ArrayList<>();

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getReceiverName() { return receiverName; }
    public void setReceiverName(String receiverName) { this.receiverName = receiverName; }
    public String getReceiverPhone() { return receiverPhone; }
    public void setReceiverPhone(String receiverPhone) { this.receiverPhone = receiverPhone; }
    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getStatusLabel() { return statusLabel(status); }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public String getCreatedAtText() {
        return createdAt == null ? "" : createdAt.format(DISPLAY_FORMAT);
    }
    public List<OrderItem_24162056> getItems() { return items; }
    public void setItems(List<OrderItem_24162056> items) { this.items = items; }

    private String statusLabel(String value) {
        if ("CONFIRMED".equals(value)) {
            return "Đã xác nhận";
        }
        if ("PREPARING".equals(value)) {
            return "Chuẩn bị hàng";
        }
        if ("SHIPPING".equals(value)) {
            return "Vận chuyển";
        }
        if ("DELIVERING".equals(value)) {
            return "Giao hàng";
        }
        if ("DELIVERED".equals(value)) {
            return "Đã giao";
        }
        if ("CANCELLED".equals(value)) {
            return "Đơn hàng hủy";
        }
        if ("RETURNED".equals(value)) {
            return "Đơn hàng hoàn";
        }
        return "Đơn hàng mới";
    }
}
