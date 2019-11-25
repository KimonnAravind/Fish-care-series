package com.example.forfishes.Model;

public class Adminorders {
    private String Address,Pincode,date,name,phone,state,time, totalamount;

    public Adminorders() {
    }

    public Adminorders(String address, String pincode, String date, String name, String phone, String state, String time, String totalamount) {
        Address = address;
        Pincode = pincode;
        this.date = date;
        this.name = name;
        this.phone = phone;
        this.state = state;
        this.time = time;
        this.totalamount = totalamount;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPincode() {
        return Pincode;
    }

    public void setPincode(String pincode) {
        Pincode = pincode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTotalamount() {
        return totalamount;
    }

    public void setTotalamount(String totalamount) {
        this.totalamount = totalamount;
    }
}
