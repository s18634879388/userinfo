package com.ninehcom.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * UserInfo实体类，将同时用于Mybatis和JPA使用
 *
 * @author shenjizhe
 * @version 1.0.0 Id 唯一标识 PhoneNumber 手机号码 NickName 昵称 Signature 个性签名 Portrait
 * 头像URL Name 姓名 Sex 性别(0:未知 1::男 2:女) Birthday 生日 Provinceid 省编码 Cityid 市编码
 * Idnum 身份证号码 Extra 扩展字段
 */
@Entity
public class UserInfo implements Serializable {
    
    @Id
    private String Id;

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }
    
    private int ChangeNickNameTimes;

    private String PhoneNumber;

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String PhoneNumber) {
        this.PhoneNumber = PhoneNumber;
    }

    private String NickName;

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String NickName) {
        this.NickName = NickName;
    }

    private String Signature;

    public String getSignature() {
        return Signature;
    }

    public void setSignature(String Signature) {
        this.Signature = Signature;
    }

    private String Portrait;

    public String getPortrait() {
        return Portrait;
    }

    public void setPortrait(String Portrait) {
        this.Portrait = Portrait;
    }

    private String Name;

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    private Integer Sex;

    public Integer getSex() {
        return Sex;
    }

    public void setSex(Integer Sex) {
        this.Sex = Sex;
    }

    private Date Birthday;

    public Date getBirthday() {
        return Birthday;
    }

    public void setBirthday(Date Birthday) {
        this.Birthday = Birthday;
    }

    private Integer Provinceid;

    public Integer getProvinceid() {
        return Provinceid;
    }

    public void setProvinceid(Integer Provinceid) {
        this.Provinceid = Provinceid;
    }

    private Integer Cityid;

    public Integer getCityid() {
        return Cityid;
    }

    public void setCityid(Integer Cityid) {
        this.Cityid = Cityid;
    }
    
    private Integer Districtid;
    public  Integer getDistrictid() {
        return Districtid;
    }
    public void setDistrictid(Integer Districtid) {
        this.Districtid = Districtid;
    }

    private String Idnum;

    public String getIdnum() {
        return Idnum;
    }

    public void setIdnum(String Idnum) {
        this.Idnum = Idnum;
    }

    private String Extra;

    public String getExtra() {
        return Extra;
    }

    public void setExtra(String Extra) {
        this.Extra = Extra;
    }
    
    private Integer RoleId;

    /**
     * @return the RoleId
     */
    public Integer getRoleId() {
        return RoleId;
    }

    /**
     * @param RoleId the RoleId to set
     */
    public void setRoleId(Integer RoleId) {
        this.RoleId = RoleId;
    }

    /**
     * @return the ChangeNickNameTimes
     */
    public int getChangeNickNameTimes() {
        return ChangeNickNameTimes;
    }

    /**
     * @param ChangeNickNameTimes the ChangeNickNameTimes to set
     */
    public void setChangeNickNameTimes(int ChangeNickNameTimes) {
        this.ChangeNickNameTimes = ChangeNickNameTimes;
    }

    private Date CreatedAt;

    public Date getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(Date createdAt) {
        CreatedAt = createdAt;
    }

    private String VIPInfo;

    /**
     * @return the VIPInfo
     */
    public String getVIPInfo() {
        return VIPInfo;
    }

    /**
     * @param VIPInfo the VIPInfo to set
     */
    public void setVIPInfo(String VIPInfo) {
        this.VIPInfo = VIPInfo;
    }
}
