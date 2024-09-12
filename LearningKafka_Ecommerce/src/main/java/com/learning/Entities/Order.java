package com.learning.Entities;

import java.math.BigDecimal;

public class Order {
    private final String id;
    private final String name;
    private final BigDecimal value;

    public Order(String id, String name, BigDecimal value){
        this.id = id;
        this.name = name;
        this.value = value;
    }
}
