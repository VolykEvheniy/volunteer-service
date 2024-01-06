package com.webtutsplus.ecommerce.dto.order;

public class OrderEmailSendDto {
    private String email;
    private Integer orderId;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }
}
