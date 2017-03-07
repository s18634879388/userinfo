/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ninehcom.util;

import com.ninehcom.entity.LogInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 *
 * @author shenjizhe
 */
@Component
public class UCAgent {

    public final static String KEY_USER_ID = "unionuserid";
    public final static String KEY_MOBILE_NUM = "mobilenum";

    private static String userUrl = "https://test-account.9h-sports.com";

    @Autowired
    ConnectAgent agent;

//    @Autowired
//    private EditconfigService configService;
//
//    @PostConstruct
//    private void init() {
//        userUrl = configService.getValue(ConfigKeys.UserUrl);
//    }

    public UCAgent() {

    }

    public String sendCheckCode(String mobilenum, String appid) throws Exception {
        String request = String.format(userUrl + "/user/getsmscode?mobilenum=%s&appid=%s",
                mobilenum, appid);
        // get sms code
//        return HttpsUtil.getAsString(request, "utf-8");
        return agent.sendRequestHttpsUTF8(request);
    }

    public String checkCode(String mobileNum, String checkCode, String appid) throws Exception {
        String request = String.format(userUrl + "/user/validatesmscode?mobilenum=%s&smscode=%s&appid=%s",
                mobileNum, checkCode, appid);//测试时使用https://test-account.9h-sports.com/user/validatesmscode?mobilenum=&smscode=&appid=  上线时使用https://api-account.9h-sports.com/user/validatesmscode?mobilenum=&smscode=&appid=
        return agent.sendRequestHttpsUTF8(request);
    }

    public String validateMobile(String mobileNum) throws Exception {
        String request = String.format(userUrl + "/user/validatemobilenum?mobilenum=%s",
                mobileNum);
        return agent.sendRequestHttpsUTF8(request);
    }

    public String validateToken(String token) throws Exception {
        String request = String.format(userUrl + "/user/validatetoken?token=%s",
                token);
        return agent.sendRequestHttpsUTF8(request);
    }

    public String register(
            String mobileNum,
            String password,
            String checkCode,
            String appID,
            LogInfo logInfo
    ) throws Exception {
        String request = String.format(userUrl + "/user/registerlogin?mobilenum=%s&smscode=%s&password=%s&appid=%s&ip=%s&systemtypeid=%s&equipmentnum=%s",
                mobileNum, checkCode, password, appID,
                logInfo.getIp(),
                logInfo.getSystemtypeid(),
                logInfo.getEquipmentnum());
        return agent.sendRequestHttpsUTF8(request);
    }

    public String login(String mobileNum, String password, String appID, LogInfo logInfo) throws Exception {
        String request = String.format(userUrl + "/user/login?mobilenum=%s&password=%s&appid=%s&ip=%s&systemtypeid=%s&equipmentnum=%s",
                mobileNum, password, appID,
                logInfo.getIp(),
                logInfo.getSystemtypeid(),
                logInfo.getEquipmentnum());

        return agent.sendRequestHttpsUTF8(request);
    }

    public String resetMobile(String token, String mobileNum, String password, String checkCode) throws Exception {
        String request = String.format(userUrl + "/user/resetmobilenum?token=%s&password=%s&mobilenum=%s&smscode=%s",
                token, password, mobileNum, checkCode);
        return agent.sendRequestHttpsUTF8(request);
    }

    public String resetPassword(String mobileNum, String password, String checkCode, String appID, LogInfo logInfo) throws Exception {
        String request = String.format(userUrl + "/user/resetpassword?mobilenum=%s&smscode=%s&password=%s&appid=%s&ip=%s&systemtypeid=%s&equipmentnum=%s",
                mobileNum, checkCode, password, appID,
                logInfo.getIp(),
                logInfo.getSystemtypeid(),
                logInfo.getEquipmentnum());
        return agent.sendRequestHttpsUTF8(request);
    }

    public String modifyPassword(String token, String oldpassword, String newpassword) throws Exception {
        String request = String.format(userUrl + "/user/modifypassword?token=%s&oldpassword=%s&newpassword=%s",
                token, oldpassword, newpassword);
        return agent.sendRequestHttpsUTF8(request);
    }

    public String getUserbytoken(String token) throws Exception {
        String request = String.format(userUrl + "/user/getbytoken?token=%s",
                token);
        return agent.sendRequestHttpsUTF8(request);
    }

    public String threePartLogin(String openId, int authorizedtypeid, String appId, LogInfo logInfo) throws Exception {
        String request = String.format(userUrl + "/user/authorizedlogin?&openid=%s&appid=%s&authorizedtypeid=%s&&ip=%s&systemtypeid=%s&equipmentnum=%s",
                openId, appId, authorizedtypeid,
                logInfo.getIp(),
                logInfo.getSystemtypeid(),
                logInfo.getEquipmentnum());
        return agent.sendRequestHttpsUTF8(request);
    }

    public String threePartLoginv2(String openId, String unionId, int authorizedtypeid, String appId, LogInfo logInfo) throws Exception {
        String request = String.format(userUrl + "/user/authorizedlogin2?&openid=%s&unionid=%s&appid=%s&authorizedtypeid=%s&&ip=%s&systemtypeid=%s&equipmentnum=%s",
                openId, unionId, appId, authorizedtypeid,
                logInfo.getIp(),
                logInfo.getSystemtypeid(),
                logInfo.getEquipmentnum());
        return agent.sendRequestHttpsUTF8(request);
    }

    public String binduser(String token, String mobileNum, String password, String checkCode) throws Exception {
        String request = String.format(userUrl + "/user/binduser?token=%s&mobilenum=%s&smscode=%s&password=%s",
                token, mobileNum, checkCode, password
        );
        return agent.sendRequestHttpsUTF8(request);
    }

    public String tokenlogin(String token) throws Exception {
        String request = String.format(userUrl + "/user/tokenlogin?token=%s",
                token);
        return agent.sendRequestHttpsUTF8(request);
    }

    public String logout(String token) throws Exception {
        String request = String.format(userUrl + "/user/logout?token=%s",
                token);
        return agent.sendRequestHttpsUTF8(request);
    }

}
