package com.javaee.pojo;

public class Course {
    private int cid;
    private String cname;
    private int period;
    private String bname;
    private String pub;
    private float price;


    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public String getPub() {
        return pub;
    }

    public void setPub(String pub) {
        this.pub = pub;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
    // 省略了getter和setter方法
}

