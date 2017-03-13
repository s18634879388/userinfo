package com.realmadrid.message.android;


import com.realmadrid.message.AndroidNotification;

/**
 * Created by lijc on 16/1/4.
 */
public class AndroidListcast extends AndroidNotification {
    public AndroidListcast(String appkey,String appMasterSecret) throws Exception {
        setAppMasterSecret(appMasterSecret);
        setPredefinedKeyValue("appkey", appkey);
        this.setPredefinedKeyValue("type", "listcast");
    }

    public void setDeviceToken(String token) throws Exception {
        setPredefinedKeyValue("device_tokens", token);
    }
}
