package com.example.bdpolice;

public class salarydata {
    private String id;
    private String name;
    private String salary;
    private String month;
    private String year;
    private String date;


    public salarydata(String id, String name, String salary, String month, String year, String date) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.month = month;
        this.year = year;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSalary() {
        return salary;
    }

    public String getMonth() {
        return month;
    }

    public String getYear() {
        return year;
    }

    public String getDate() {
        return date;
    }
}
