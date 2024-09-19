package com.learning;

import java.math.BigDecimal;

public class Order {
    private final String orderId;
    private final String name;
    private final BigDecimal value;
    private final String email;

    public Order(String orderId, String name, BigDecimal value, String email){
        this.orderId = orderId;
        this.name = name;
        this.value = value;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getValue() {
        return value;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", name='" + name + '\'' +
                ", value=" + value +
                ", email='" + email + '\'' +
                '}';
    }
}
