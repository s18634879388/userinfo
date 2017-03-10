package com.realmadrid.message.ios;


import com.realmadrid.message.IOSNotification;

public class IOSListcast extends IOSNotification {
    public IOSListcast() {
        try {
            this.setPredefinedKeyValue("type", "listcast");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
