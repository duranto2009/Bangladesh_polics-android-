package com.example.bdpolice;

public class noticedata {
    private String month;
    private String date;
    private String year;
    private String subject;
    private String message;

    public noticedata(String subject ,String notice, String date, String month, String year) {
        this.month = month;
        this.subject = subject;
        this.message = notice;
        this.date = date;
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public String getSubject() {
        return subject;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }

    public String getYear() {
        return year;
    }
}
