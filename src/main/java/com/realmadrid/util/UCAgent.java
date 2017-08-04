/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realmadrid.util;

import com.realmadrid.entity.LogInfo;
import org.apache.log4j.spi.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author shenjizhe
 */
@Component
public class UCAgent {

    public final static String KEY_USER_ID = "unionuserid";
    public final static String KEY_MOBILE_NUM = "mobilenum";

//    private static String userUrl = "http://test-account.9h-sports.com";
    private Logger logger = org.slf4j.LoggerFactory.getLogger(UCAgent.class);
    @Value("${userUrl}")
    private  String userUrl;

    public UCAgent() {

    }

    public String sendCheckCode(String mobilenum, String appid) throws Exception {
        String request = NinehStringUtils.format(userUrl + "/user/getsmscode?mobilenum=%s&appid=%s",
                mobilenum, appid);
        // get sms code
        return HttpClients.get(request);
    }

    public String checkCode(String mobileNum, String checkCode, String appid) throws Exception {
        String request = NinehStringUtils.format(userUrl + "/user/validatesmscode?mobilenum=%s&smscode=%s&appid=%s",
                mobileNum, checkCode, appid);//测试时使用https://test-account.9h-sports.com/user/validatesmscode?mobilenum=&smscode=&appid=  上线时使用https://api-account.9h-sports.com/user/validatesmscode?mobilenum=&smscode=&appid=
        return HttpClients.get(request);
    }

    public String validateMobile(String mobileNum) throws Exception {
        String request = NinehStringUtils.format(userUrl + "/user/validatemobilenum?mobilenum=%s",
                mobileNum);
        return HttpClients.get(request);
    }

    public String validateToken(String token) throws Exception {
        String request = NinehStringUtils.format(userUrl + "/user/validatetoken?token=%s",
                token);
        return HttpClients.get(request);
    }

    public String register(
            String mobileNum,
            String password,
            String checkCode,
            String appID,
            LogInfo logInfo
    ) throws Exception {
        String request = NinehStringUtils.format(userUrl + "/user/registerlogin?mobilenum=%s&smscode=%s&password=%s&appid=%s&ip=%s&systemtypeid=%s&equipmentnum=%s",
                mobileNum, checkCode, password, appID,
                logInfo.getIp(),
                logInfo.getSystemtypeid(),
                logInfo.getEquipmentnum());
        return HttpClients.get(request);
    }

    public String login(String mobileNum, String password, String appID, LogInfo logInfo) throws Exception {
        String request = NinehStringUtils.format(userUrl + "/user/login?mobilenum=%s&password=%s&appid=%s&ip=%s&systemtypeid=%s&equipmentnum=%s",
                mobileNum, password, appID,
                logInfo.getIp(),
                logInfo.getSystemtypeid(),
                logInfo.getEquipmentnum());

        return HttpClients.get(request);
    }

    public String resetMobile(String token, String mobileNum, String password, String checkCode) throws Exception {
        String request = NinehStringUtils.format(userUrl + "/user/resetmobilenum?token=%s&password=%s&mobilenum=%s&smscode=%s",
                token, password, mobileNum, checkCode);
        return HttpClients.get(request);
    }

    public String resetPassword(String mobileNum, String password, String checkCode, String appID, LogInfo logInfo) throws Exception {
        String request = NinehStringUtils.format(userUrl + "/user/resetpassword?mobilenum=%s&smscode=%s&password=%s&appid=%s&ip=%s&systemtypeid=%s&equipmentnum=%s",
                mobileNum, checkCode, password, appID,
                logInfo.getIp(),
                logInfo.getSystemtypeid(),
                logInfo.getEquipmentnum());
        return HttpClients.get(request);
    }


    public String getUserbytoken(String token) throws Exception {
        String request = NinehStringUtils.format(userUrl + "/user/getbytoken?token=%s",
                token);
        return HttpClients.get(request);
    }

    public String threePartLoginv2(String openId, String unionId, int authorizedtypeid, String appId, LogInfo logInfo) throws Exception {
        String request = NinehStringUtils.format(userUrl + "/user/authorizedlogin2?&openid=%s&unionid=%s&appid=%s&authorizedtypeid=%s&&ip=%s&systemtypeid=%s&equipmentnum=%s",
                openId==null?"":openId, unionId, appId, authorizedtypeid,
                logInfo.getIp(),
                logInfo.getSystemtypeid(),
                logInfo.getEquipmentnum());
        logger.info("threePartLoginv2-------url------>"+request);
        return HttpClients.get(request);
    }





    public String logout(String token) throws Exception {
        String request = NinehStringUtils.format(userUrl + "/user/logout?token=%s",
                token);
        return HttpClients.get(request);
    }

}
