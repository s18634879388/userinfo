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

    private String NickName;

    private String PhoneNumber;

    private String unionId;

    private String openid;

    private Date CreatedAt;

    private Date UpdatedAt;

    private String token;

    private String password;

    private String headimgurl;

//    private int ChangeNickNameTimes;
//
//    private String Signature;
//
//    private String Portrait;
//
//    private String Name;
//
//    private Integer Sex;
//
//    private Date Birthday;
//
//    private Integer Provinceid;
//
//    private Integer Cityid;
//
//    private Integer Districtid;
//
//    private String Idnum;
//
//    private String Extra;
//
//    private Integer RoleId;
//
//    private String VIPInfo;


    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public Date getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(Date createdAt) {
        CreatedAt = createdAt;
    }

    public Date getUpdatedAt() {
        return UpdatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        UpdatedAt = updatedAt;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }
}
