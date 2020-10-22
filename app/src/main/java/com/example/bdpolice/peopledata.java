package com.example.bdpolice;

public class peopledata {
    private String id;
    private String serial;
    private String name;
    private String batch;
    private String rank;
    private String value;
    private String image;


    public peopledata(String id, String name, String image,String rank,String batch,String serial,String value) {
        this.id = id;
        this.name = name;
        this.batch = batch;
        this.rank = rank;
        this.serial = serial;
        this.value = value;
        this.image =UrlAddress.ROOT_ImageURI+ image;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getrank() {
        return rank;
    }

    public String getbatch() {
        return batch;
    }

    public String getImage() {
        return image;
    }

    public String getSerial() {
        return serial;
    }

    public String getValue() {
        return value;
    }
}
