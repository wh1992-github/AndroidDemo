package com.example.mixture.bean;
/**
 * 封装 Mac Device 相关逻辑的类。
 */

public class MacDevice {
    public int xuhao;
    public String mac;
    public String device;

    public MacDevice() {
        xuhao = 0;
        mac = "";
        device = "";
    }

    public MacDevice(String mac, String device) {
        xuhao = 0;
        this.mac = mac;
        this.device = device;
    }
}
