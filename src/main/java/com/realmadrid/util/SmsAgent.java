/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realmadrid.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;

/**
 *
 * @author shenjizhe
 */
@Component
public class SmsAgent {

    private static String smsUrl = "https://test-sms.9h-sports.com";


    public String snedMessage(String mobilenum, String appid, String contents) throws Exception {
//        String CheckCodeText = configService.getValue(ConfigKeys.CheckCodeText);
        String CheckCodeText =  "【北京国安】您的验证码是：%s，有效期为30分钟。";
        contents = CheckCodeText.replace("%s", contents);
        contents = URLEncoder.encode(contents, "utf-8");
        String request = String.format(smsUrl + "/smsservice/send?mobilenum=%s&appid=%s&contents=%s",
                mobilenum, appid, contents);
        // get sms code
//        return HttpsUtil.getAsString(request, "utf-8");
        return HttpClients.get(request);
    }
    
    public String snedOriginMessage(String mobilenum, String appid, String contents) throws Exception {
        contents = URLEncoder.encode(contents, "utf-8");
        String request = String.format(smsUrl + "/smsservice/send?mobilenum=%s&appid=%s&contents=%s",
                mobilenum, appid, contents);
        // get sms code
//        return HttpsUtil.getAsString(request, "utf-8");
        return HttpClients.get(request);
    }
}
