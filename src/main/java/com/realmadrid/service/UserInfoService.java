package com.realmadrid.service;

import com.realmadrid.entity.LogInfo;
import com.realmadrid.entity.Message;
import com.realmadrid.entity.UserInfo;
import com.realmadrid.mapper.LogInfoMapper;
import com.realmadrid.mapper.UserInfoMapper;
import com.realmadrid.message.AndroidNotification;
import com.realmadrid.message.PushClient;
import com.realmadrid.message.android.AndroidBroadcast;
import com.realmadrid.message.android.AndroidUnicast;
import com.realmadrid.message.ios.IOSBroadcast;
import com.realmadrid.message.ios.IOSUnicast;
import com.realmadrid.util.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Logger;

/**
 *
 */
@Service
public class UserInfoService {

    private static final Logger LOG = Logger.getLogger(UserInfoService.class.getName());

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private UCAgent ucAgent;

    @Autowired
    private SmsAgent smsAgent;

    @Autowired
    LogInfoMapper logInfoMapper;

    private PushClient client = new PushClient();

    /**
     * 新用户的注册功能
     */
    public Result register(
            String mobileNum,
            String password,
            String checkCode,
            String appID,
            LogInfo logInfo
    ) throws JSONException, NoSuchAlgorithmException, UnsupportedEncodingException {
        //id(生成规则) nickname（）phone  openid  unionid createdAt updatedAt（）
        String response = null;
        try {
            response = ucAgent.register(mobileNum, password, checkCode, appID, logInfo);
        } catch (Exception ex) {
            return Result.Fail(ErrorCode.UserCenterCantConnect, ex);
        }
        Result result = Result.getResult(response);
        if (result.isSuccess()) {
            String userId = result.getValue(UCAgent.KEY_USER_ID);

            if (userId == null || userId.isEmpty()) {
                return Result.Fail(ErrorCode.UserIdIsEmpty);
            }
            String nickname = Base62Utils.getNextAccount();
            UserInfo userInfo = new UserInfo();
            userInfo.setId(userId);
            String salt = Md5Utils.getNextSalt();
            userInfo.setSalt(salt);
            userInfo.setPassword(Md5Utils.md5(password+salt));
            userInfo.setNickName(nickname);
            userInfo.setPhoneNumber(mobileNum);
            int ret = userInfoMapper.insertUser(userInfo);
            if (ret != 1) {
                return Result.Fail(ErrorCode.UserInsertDBFail);
            }
            logInfo.setUserId(userId);
            logInfoMapper.addLogInfo(logInfo);

        }
        return result;
    }


    /**
     * 发送验证码
     */
    public Result sendCheckCode(String mobilenum, String stage,String appid) throws JSONException {
        String response1 = null;
        try {
            response1 = ucAgent.validateMobile(mobilenum);
            JSONObject jsonObject = new JSONObject(response1);
            if (jsonObject.getInt("code")!=0){
                if (stage.equals("register")){
                    return Result.Fail(ErrorCode.UserHasRegister);
                }
            }else {
                if (stage.equals("reset")){
                    return Result.Fail(ErrorCode.UserHasNotRegister);
                }
            }
        } catch (Exception e) {
            return Result.Fail(ErrorCode.UserCenterCantConnect, e);
        }

        String response = null;
        try {
            response = ucAgent.sendCheckCode(mobilenum, appid);
        } catch (Exception ex) {
            return Result.Fail(ErrorCode.UserCenterCantConnect, ex);
        }

        JSONObject obj = new JSONObject(response);
        Result result = Result.getResult(obj);
        // send sms code
        if (result.isSuccess()) {
            String code = obj.getString("smscode");
            LOG.info("\n send smscode=" + code + "\n");
            try {
                response = smsAgent.snedMessage(mobilenum, appid, code);
            } catch (Exception ex) {
                return Result.Fail(ErrorCode.SMSCantConnect, ex);
            }
            obj = new JSONObject(response);
            result = Result.getResult(obj);
            return result;
        }
        return result;
    }
    /**
     * 比较验证码的功能 验证码半小时内有效
     */
    public Result checkCode(String mobileNum, String checkCode, String appid) throws JSONException {

        String response = null;
        try {
            response = ucAgent.checkCode(mobileNum, checkCode, appid);
        } catch (Exception ex) {
            return Result.Fail(ErrorCode.UserCenterCantConnect, ex);
        }

        return Result.getResult(response);
    }
    /**
     * 检查手机号是否可用
     */
    public Result validateMobile(String mobileNum) throws JSONException {
        String response = null;
        try {
            response = ucAgent.validateMobile(mobileNum);
        } catch (Exception ex) {
            return Result.Fail(ErrorCode.UserCenterCantConnect, ex);
        }
        return Result.getResult(response);
    }

    /**
     * 已有用户的登录功能
     */
    public Result login(String mobileNum, String password, String appID, LogInfo logInfo) throws JSONException {
        String response = null;
        try {
            response = ucAgent.login(mobileNum, password, appID, logInfo);
        } catch (Exception ex) {
            return Result.Fail(ErrorCode.UserCenterCantConnect, ex);
        }
        Result result = Result.getResult(response);
        if (result.isSuccess()) {
            String userId = result.getValue(UCAgent.KEY_USER_ID);
            if (userId == null || userId.isEmpty()) {
                return Result.Fail(ErrorCode.UserIdIsEmpty);
            }
            UserInfo user = userInfoMapper.selectUserInfoById(userId);
            if (user == null) {
                String nickname = Base62Utils.getNextAccount();
                UserInfo userInfo = new UserInfo();
                userInfo.setId(userId);
                userInfo.setNickName(nickname);
                userInfo.setPhoneNumber(mobileNum);
                String salt = Md5Utils.getNextSalt();
                userInfo.setSalt(salt);
                userInfo.setPassword(Md5Utils.md5(password+salt));
                int ret = userInfoMapper.insertUser(userInfo);
                if (ret != 1) {
                    return Result.Fail(ErrorCode.UserInsertDBFail);
                }
            }else {
                user.setSalt(password);
                user.setPassword(Md5Utils.md5(password));
                userInfoMapper.updateUserPass(user);
            }
            logInfo.setUserId(userId);
            logInfoMapper.addLogInfo(logInfo);
        }
        return result;

    }


    public Result threePartLoginv2(String openId, String unionId, String accessToken, String refreshToken, int authorizedtypeid,
                                   String appId,String nickName ,String headimgurl, LogInfo logInfo) throws JSONException, IOException {
        String response = null;
        try {
            response = ucAgent.threePartLoginv2(openId, unionId, authorizedtypeid, appId, logInfo);
        } catch (Exception ex) {
            return Result.Fail(ErrorCode.UserCenterCantConnect, ex);
        }
        Result result = Result.getResult(response);
        if (result.isSuccess()) {
            String userId = result.getValue(UCAgent.KEY_USER_ID);
            if (userId == null || userId.isEmpty()) {
                return Result.Fail(ErrorCode.UserIdIsEmpty);
            }
            UserInfo user = userInfoMapper.selectUserInfoById(userId);
            if (user == null) {
                user = new UserInfo();
                user.setNickName(nickName);
                user.setHeadimgurl(headimgurl);
                user.setId(userId);
                user.setUnionId(unionId);
                user.setAccesstoken(accessToken);
                user.setRefreshtoken(refreshToken);
                int ret = userInfoMapper.insertUser(user);
                if (ret != 1) {
                    return Result.Fail(ErrorCode.UserInsertDBFail);
                }
            }
            logInfo.setUserId(userId);
            logInfoMapper.addLogInfo(logInfo);
        }
        return result;



    }
    /**
     * 检查token
     */
    public Result validateToken(String token) throws JSONException {

        String response = null;
        try {
            response = ucAgent.validateToken(token);
        } catch (Exception ex) {
            return Result.Fail(ErrorCode.UserCenterCantConnect, ex);
        }
        return Result.getResult(response);
    }
    /**
     * 用户重置密码----------查看接口是否返回userid
     */
    public Result resetPassword(String mobileNum, String password, String checkCode, String appID, LogInfo logInfo) throws JSONException {
        String response = null;
        try {
            response = ucAgent.resetPassword(mobileNum, password, checkCode, appID, logInfo);
        } catch (Exception ex) {
            return Result.Fail(ErrorCode.UserCenterCantConnect, ex);
        }
        Result result = Result.getResult(response);
        if (result.isSuccess()) {
            String userId = result.getValue(UCAgent.KEY_USER_ID);
            if (userId == null || userId.isEmpty()) {
                return Result.Fail(ErrorCode.UserIdIsEmpty);
            }
            logInfo.setUserId(userId);
            logInfoMapper.addLogInfo(logInfo);
        }
        return Result.getResult(response);

    }
    /**
     * 登出
     */
    public Result logout(String token) throws JSONException {

        String response = null;
        try {
            response = ucAgent.logout(token);
        } catch (Exception ex) {
            return Result.Fail(ErrorCode.UserCenterCantConnect, ex);
        }
        return Result.getResult(response);
    }

    /**
     * 根据token取得用户信息
     */
    public Result getUserbytoken(String token) throws JSONException {

        String response = null;
        try {
            response = ucAgent.getUserbytoken(token);
        } catch (Exception ex) {
            return Result.Fail(ErrorCode.UserCenterCantConnect, ex);
        }

        Result result = Result.getResult(response);

        if (result.isSuccess()) {
            String userId = result.getValue(UCAgent.KEY_USER_ID);
            String mobileNum = result.getValue(UCAgent.KEY_MOBILE_NUM);

            UserInfo user = userInfoMapper.selectUserInfoById(userId);
            if (user != null) {
                HashMap<String,String> map = new HashMap<>();
                map.put("user_id",userId);
                map.put("union_id",user.getUnionId());
                map.put("open_id",user.getOpenid());
                map.put("access_token",user.getAccesstoken());
                map.put("refresh_token",user.getRefreshtoken());
                //登录类型
                map.put("user_type","");
                map.put("phone_number",mobileNum);
                map.put("nick_name",user.getNickName());
                map.put("avatar",user.getHeadimgurl());


                user.setPhoneNumber(mobileNum);
                result.setTag(user);
            }
        }
        return result;
    }


    public Result push(Message message) throws Exception {
        //123.59.84.71  消息推送  device token     登录记录    token取信息返回格式    加队列
        if ("union_cast".equals(message.getType())){
            AndroidUnicast unicast = new AndroidUnicast(message.getAppKey(),"cvez4r8js0xwbghysn7l8kfbrosygtfz");
            // TODO Set your device token
            unicast.setDeviceToken( "Aj218wKYETsQdjuqV3454pvhuAFRG1i0o0s5nDRi92cj");
            unicast.setTicker( "Android unicast ticker");
            unicast.setTitle(  "中文的title");
            unicast.setText(   "Android unicast text");
            unicast.goAppAfterOpen();
            unicast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
            // For how to register a test device, please see the developer doc.
            unicast.setProductionMode();
            // Set customized fields
            unicast.setExtraField("test", "helloworld");
            client.send(unicast);
        }
        return null;
    }

    public void sendAndroidBroadcast() throws Exception {
        AndroidBroadcast broadcast = new AndroidBroadcast("","");
        broadcast.setTicker( "Android broadcast ticker");
        broadcast.setTitle(  "中文的title");
        broadcast.setText(   "Android broadcast text");
        broadcast.goAppAfterOpen();
        broadcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
        // TODO Set 'production_mode' to 'false' if it's a test device.
        // For how to register a test device, please see the developer doc.
        broadcast.setProductionMode();
        // Set customized fields
        broadcast.setExtraField("test", "helloworld");
        client.send(broadcast);
    }
    public void sendAndroidUnicast() throws Exception {
        AndroidUnicast unicast = new AndroidUnicast("","");
        // TODO Set your device token
        unicast.setDeviceToken( "your device token");
        unicast.setTicker( "Android unicast ticker");
        unicast.setTitle(  "中文的title");
        unicast.setText(   "Android unicast text");
        unicast.goAppAfterOpen();
        unicast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
        // TODO Set 'production_mode' to 'false' if it's a test device.
        // For how to register a test device, please see the developer doc.
        unicast.setProductionMode();
        // Set customized fields
        unicast.setExtraField("test", "helloworld");
        client.send(unicast);
    }
    public void sendIOSUnicast() throws Exception {
        IOSUnicast unicast = new IOSUnicast("","");
        // TODO Set your device token
        unicast.setDeviceToken( "xx");
        unicast.setAlert("IOS 单播测试");
        unicast.setBadge( 0);
        unicast.setSound( "default");
        // TODO set 'production_mode' to 'true' if your app is under production mode
        unicast.setTestMode();
        // Set customized fields
        unicast.setCustomizedField("test", "helloworld");
        client.send(unicast);
    }
    public void sendIOSBroadcast() throws Exception {
        IOSBroadcast broadcast = new IOSBroadcast("","");

        broadcast.setAlert("IOS 广播测试");
        broadcast.setBadge( 0);
        broadcast.setSound( "default");
        // TODO set 'production_mode' to 'true' if your app is under production mode
        broadcast.setTestMode();
        // Set customized fields
        broadcast.setCustomizedField("test", "helloworld");
        client.send(broadcast);
    }


}
