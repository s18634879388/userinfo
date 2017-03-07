/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ninehcom.entity;

/**
 *
 * @author Administrator
 */
public class LogInfo {

    private String ip;//ip地址
    private String systemtypeid;//设备系统类型1：Android 2：IOS 3：WP 4：PC
    private String equipmentnum;//设备号

    public LogInfo() {
    }

    public LogInfo(String ip, String systemtypeid, String equipmentnum) {
        this.ip = ip;
        this.systemtypeid = systemtypeid;
        this.equipmentnum = equipmentnum;
    }

    
    /**
     * @return the ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * @param ip the ip to set
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * @return the systemtypeid
     */
    public String getSystemtypeid() {
        return systemtypeid;
    }

    /**
     * @param systemtypeid the systemtypeid to set
     */
    public void setSystemtypeid(String systemtypeid) {
        this.systemtypeid = systemtypeid;
    }

    /**
     * @return the equipmentnum
     */
    public String getEquipmentnum() {
        return equipmentnum;
    }

    /**
     * @param equipmentnum the equipmentnum to set
     */
    public void setEquipmentnum(String equipmentnum) {
        this.equipmentnum = equipmentnum;
    }
}
