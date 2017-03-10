package com.realmadrid.message.android;


import com.realmadrid.message.AndroidNotification;

/**
 * Created by lijc on 16/1/4.
 */
public class AndroidListcast extends AndroidNotification {
    public AndroidListcast() {
        try {
            this.setPredefinedKeyValue("type", "listcast");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
