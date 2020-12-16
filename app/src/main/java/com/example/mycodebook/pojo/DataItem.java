package com.example.mycodebook.pojo;

public class DataItem {
        private Integer id;

        private String num;
         private String info;
         private String pwd;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DataItem(Integer id, String num, String info, String pwd) {
        this.id = id;
        this.num = num;
        this.info = info;
        this.pwd = pwd;
    }
    public DataItem() {
    }

    public DataItem(String num, String info) {
        this.num = num;
        this.info = info;
    }

    public DataItem(String num, String info, String pwd) {
        this.num = num;
        this.info = info;
        this.pwd = pwd;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }



}
