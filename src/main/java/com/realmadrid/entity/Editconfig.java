package com.realmadrid.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * Editconfig实体类，将同时用于Mybatis和JPA使用
 * @author shenjizhe
 * @version 1.0.0
Id
Key
Value
Remark
CreateTime
UpdateTime
 */
@Entity
public class Editconfig {

    public Editconfig() {
    }

    public Editconfig(int Id, String Key, String Value, String Remark) {
        this.Id = Id;
        this.Key = Key;
        this.Value = Value;
        this.Remark = Remark;
    }

    @Id
    private int Id;
    public  int getId() {
        return Id;
    }
    public void setId(int Id) {
        this.Id = Id;
    }

    private String Key;
    public  String getKey() {
        return Key;
    }
    public void setKey(String Key) {
        this.Key = Key;
    }

    private String Value;
    public  String getValue() {
        return Value;
    }
    public void setValue(String Value) {
        this.Value = Value;
    }

    private String Remark;
    public  String getRemark() {
        return Remark;
    }
    public void setRemark(String Remark) {
        this.Remark = Remark;
    }

    private Date CreateTime;
    public  Date getCreateTime() {
        return CreateTime;
    }
    public void setCreateTime(Date CreateTime) {
        this.CreateTime = CreateTime;
    }

    private Date UpdateTime;
    public  Date getUpdateTime() {
        return UpdateTime;
    }
    public void setUpdateTime(Date UpdateTime) {
        this.UpdateTime = UpdateTime;
    }

}
