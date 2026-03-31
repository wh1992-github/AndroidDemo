package com.example.mixture.bean;
/**
 * 封装 Device Name 相关逻辑的类。
 */

public class DeviceName {
    public int xuhao;
    public String device;
    public String name;

    public DeviceName() {
        xuhao = 0;
        device = "";
        name = "";
    }

    public DeviceName(String device, String name) {
        xuhao = 0;
        this.device = device;
        this.name = name;
    }

}
