package com.realmadrid.entity;

import javax.persistence.Entity;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/3/10.
 */
//@Entity
public class Message {
    private String appKey = "55d57c8b67e58e13a0002a00";
    private String device_tokens = "Aj218wKYETsQdjuqV3454pvhuAFRG1i0o0s5nDRi92cj";
    private String sender;
    private String type;
    private List<String> receives;
    private String ticker;
    private String title;
    private String text;
    private HashMap<String,String> extra;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getReceives() {
        return receives;
    }

    public void setReceives(List<String> receives) {
        this.receives = receives;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public HashMap<String, String> getExtra() {
        return extra;
    }

    public void setExtra(HashMap<String, String> extra) {
        this.extra = extra;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getDevice_tokens() {
        return device_tokens;
    }

    public void setDevice_tokens(String device_tokens) {
        this.device_tokens = device_tokens;
    }
}
