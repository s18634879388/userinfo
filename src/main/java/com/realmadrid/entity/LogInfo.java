package com.realmadrid.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 *
 */
@Entity
public class LogInfo {
    @Id
    private int id;
    private String ip;//ip地址
    private String systemtypeid;//设备系统类型1：Android 2：IOS 3：WP 4：PC
    private String equipmentnum;//设备号
    private String userId;
    private Date activedAt;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getActivedAt() {
        return activedAt;
    }

    public void setActivedAt(Date activedAt) {
        this.activedAt = activedAt;
    }
}
