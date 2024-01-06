package com.webtutsplus.ecommerce.dto.order;

import java.util.List;

public class OrderMilitaryDto {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;
    private String postalService;
    private double totalCost;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalService() {
        return postalService;
    }

    public void setPostalService(String postalService) {
        this.postalService = postalService;
    }

//    public List<OrderItemsDto> getOrderItems() {
//        return orderItems;
//    }
//
//    public void setOrderItems(List<OrderItemsDto> orderItems) {
//        this.orderItems = orderItems;
//    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }


}
