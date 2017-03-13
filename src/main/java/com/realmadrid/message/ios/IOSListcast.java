package com.realmadrid.message.ios;


import com.realmadrid.message.IOSNotification;

public class IOSListcast extends IOSNotification {
    public IOSListcast(String appkey,String appMasterSecret) throws Exception{
        setAppMasterSecret(appMasterSecret);
        setPredefinedKeyValue("appkey", appkey);
        this.setPredefinedKeyValue("type", "listcast");
    }

    public void setDeviceToken(String token) throws Exception {
        setPredefinedKeyValue("device_tokens", token);
    }
}
