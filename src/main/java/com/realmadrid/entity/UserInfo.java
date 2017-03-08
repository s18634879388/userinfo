package com.realmadrid.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 *
 */
@Entity
public class UserInfo implements Serializable {

    @Id
    private String Id;      //唯一id

    private String NickName;    //昵称

    private String PhoneNumber; //电话

    private String unionId;     //微信unionid

    private String openid;      //微信openid

    private Date CreatedAt;     //创建时间

    private Date UpdatedAt;     //更改时间

    private String password;    //密码

    private String headimgurl;  //微信头像url

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
