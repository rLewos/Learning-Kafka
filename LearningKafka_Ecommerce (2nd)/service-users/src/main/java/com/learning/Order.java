package com.learning;

import java.math.BigDecimal;

public class Order {

    private final String id;
    private final String name;
    private final BigDecimal value;
    private final String email;

    public Order(String id, String name, BigDecimal value, String email){
        this.id = id;
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
    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
}
