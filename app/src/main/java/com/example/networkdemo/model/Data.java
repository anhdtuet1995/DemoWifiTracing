package com.example.networkdemo.model;

public class Data {

    private long time;
    private String mac;
    private int status;

    public Data(long time, String mac, int status) {
        this.time = time;
        this.mac = mac;
        this.status = status;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
