package com.learning;

public class User {
    private final String id;

    public User(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getReportPath() {
        return "target/reports/"+ this.getId() + "-report.txt";
    }
}
