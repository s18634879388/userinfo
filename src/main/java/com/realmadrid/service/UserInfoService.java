package com.realmadrid.service;

import com.realmadrid.entity.LogInfo;
import com.realmadrid.entity.Message;
import com.realmadrid.entity.UserInfo;
import com.realmadrid.mapper.LogInfoMapper;
import com.realmadrid.mapper.UserInfoMapper;
import com.realmadrid.message.AndroidNotification;
import com.realmadrid.message.PushClient;
import com.realmadrid.message.android.AndroidBroadcast;
import com.realmadrid.message.android.AndroidListcast;
import com.realmadrid.message.android.AndroidUnicast;
import com.realmadrid.message.ios.IOSBroadcast;
import com.realmadrid.message.ios.IOSListcast;
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
import java.util.List;
import java.util.Set;
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

//    private static String appKey = "58c21df88f4a9d3aec000855";
//    private static String appMasterSecret = "ktfyoiftechey1jzspbk2q0bpj41jmfc";

    private static String appKey = "58c8a175310c9367b7000e33";
    private static String appMasterSecret = "dxqxbqpzxjyta3lt3dx142frdo8skhph";

    private static String iosAppKey = "";

    private static String iosAppMasterSecret = "";

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
//                    return Result.Fail(ErrorCode.UserHasRegister);
                    return Result.getResult(response1);
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
        Result result = Result.getResult(response);
//        if (result.getErrCode()==1002||result.getErrCode()==114||result.getErrCode()==113){
//            result.setMessage("验证码输入错误,请重新输入");
//        }
        return result;
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
            LogInfo logInfo1 = logInfoMapper.selectLogInfoByUser(userId);
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
                String salt = Md5Utils.getNextSalt();
                user.setSalt(salt);
                user.setPassword(Md5Utils.md5(password+salt));
                userInfoMapper.updateUserPass(user);
            }
            if (logInfo1==null){
                logInfo.setUserId(userId);
                logInfoMapper.addLogInfo(logInfo);
            }else {
                logInfo1.setSystemtypeid(logInfo.getSystemtypeid());
                logInfo1.setEquipmentnum(logInfo.getEquipmentnum());
                logInfo1.setIp(logInfo.getIp());
                logInfoMapper.updateLogInfo(logInfo1);
            }
        }
//        if (result.getErrCode()==102){
//            result.setMessage("手机号输入错误,请重新输入");
//        }
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
            LogInfo logInfo1 = logInfoMapper.selectLogInfoByUser(userId);
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
            if (logInfo1==null){
                logInfo.setUserId(userId);
                logInfoMapper.addLogInfo(logInfo);
            }else {
                logInfo1.setSystemtypeid(logInfo.getSystemtypeid());
                logInfo1.setEquipmentnum(logInfo.getEquipmentnum());
                logInfo1.setIp(logInfo.getIp());
                logInfoMapper.updateLogInfo(logInfo1);
            }
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
            LOG.info("token--------------"+token);
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
        //若在其他平台注册过，找回密码时查询本地数据库无信息，故不记录登录设备信息
//        Result result = Result.getResult(response);
//        if (result.isSuccess()) {
//            UserInfo userInfo = userInfoMapper.selectUserInfoByPhone(mobileNum);
//            if (userInfo == null) {
//                return Result.Fail(ErrorCode.UserIdIsEmpty);
//            }
//            String userId = userInfo.getId();
//            LogInfo logInfo1 = logInfoMapper.selectLogInfoByUser(userId);
//            if (logInfo1==null){
//                logInfo.setUserId(userId);
//                logInfoMapper.addLogInfo(logInfo);
//            }else {
//                logInfo1.setSystemtypeid(logInfo.getSystemtypeid());
//                logInfo1.setEquipmentnum(logInfo.getEquipmentnum());
//                logInfo1.setIp(logInfo.getIp());
//                logInfoMapper.updateLogInfo(logInfo1);
//            }
//        }
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
            LogInfo logInfo1 = logInfoMapper.selectLogInfoByUser(userId);
            if (user != null) {
                HashMap<String,String> map = new HashMap<>();
                map.put("user_id",userId);
                map.put("union_id",user.getUnionId());
                map.put("open_id",user.getOpenid());
                map.put("access_token",user.getAccesstoken());
                map.put("refresh_token",user.getRefreshtoken());
                map.put("user_type","");
                //登录类型
                if (logInfo1!=null){
                    map.put("user_type",logInfo1.getSystemtypeid());
                }
                map.put("phone_number",mobileNum);
                map.put("nick_name",user.getNickName());
                map.put("avatar",user.getHeadimgurl());


                result.setTag(map);
            }
        }
        return result;
    }


    public Result push(Message message) throws Exception {
        //123.59.84.71  消息推送  device token     登录记录    token取信息返回格式    加队列
        if ("union_cast".equals(message.getType())){
            LogInfo logInfo1 = logInfoMapper.selectLogInfoByUser(message.getReceives().get(0));
            if ("2".equals(logInfo1.getSystemtypeid())){
                sendIOSUnicast(message);
            }else if ("1".equals(logInfo1.getSystemtypeid())){
                sendAndroidUnicast(message);
            }
        }else if ("listcast".equals(message.getType())){
            List<String> userIds = message.getReceives();
            String androidTokens = "";
            String iosTokens = "";
            for (String userid:userIds
                 ) {
                LogInfo logInfo = logInfoMapper.selectLogInfoByUser(userid);
                if(logInfo!=null){
                    if (logInfo.getEquipmentnum().length()>44){
                        iosTokens = iosTokens+logInfo.getEquipmentnum()+",";
                    }else {
                        androidTokens = androidTokens+logInfo.getEquipmentnum()+",";
                    }
                }
            }

            if (androidTokens.endsWith(",")){
                androidTokens = androidTokens.substring(0,androidTokens.length()-1);
            }
            if (iosTokens.endsWith(",")){
                iosTokens = iosTokens.substring(0,iosTokens.length()-1);
            }
            if (androidTokens.length()>0){
                sendAndroidListcast(message,androidTokens);
            }
            if (iosTokens.length()>0){
                sendIOSListCast(message,iosTokens);
            }
        }else if("broadcast".equals(message.getType())){
            sendAndroidBroadcast(message);
            sendIOSBroadcast(message);
        }

        return null;
    }

    public void sendAndroidBroadcast(Message message) throws Exception {
        AndroidBroadcast broadcast = new AndroidBroadcast(appKey,appMasterSecret);
        broadcast.setTicker(message.getTicker());
        broadcast.setTitle(message.getTitle());
        broadcast.setText(message.getText());
        broadcast.goAppAfterOpen();
        broadcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
        // TODO Set 'production_mode' to 'false' if it's a test device.
        // For how to register a test device, please see the developer doc.
        // Set customized fields
        HashMap<String,String> extra = message.getExtra();
        Set<String> set = extra.keySet();
        for (String key:set
                ) {
            broadcast.setExtraField(key, extra.get(key));
        }
        broadcast.setExtraField("sender","");
        broadcast.setProductionMode();
//        broadcast.setTestMode();
        client.send(broadcast);
    }
    public void sendAndroidListcast(Message message,String androidTokens) throws Exception {
        AndroidListcast listCast = new AndroidListcast(appKey,appMasterSecret);
        // TODO Set your device token
        listCast.setDeviceToken(androidTokens);
        listCast.setTicker(message.getTicker());
        listCast.setTitle(message.getTitle());
        listCast.setText(message.getText());
        listCast.goAppAfterOpen();
        listCast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
        // TODO Set 'production_mode' to 'false' if it's a test device.
        // For how to register a test device, please see the developer doc.
        // Set customized fields
        HashMap<String,String> extra = message.getExtra();
        Set<String> set = extra.keySet();
        for (String key:set
                ) {
            listCast.setExtraField(key, extra.get(key));
        }
        listCast.setExtraField("sender",message.getSender());
        listCast.setProductionMode();
//        listCast.setTestMode();
        client.send(listCast);
    }
    public void sendAndroidUnicast(Message message) throws Exception {
        AndroidUnicast unicast = new AndroidUnicast(appKey,appMasterSecret);
        // TODO Set your device token
        LogInfo logInfo = logInfoMapper.selectLogInfoByUser(message.getReceives().get(0));
        if (logInfo==null){
            LOG.info("不存在的设备信息");
            return;
        }

        unicast.setDeviceToken(logInfo.getEquipmentnum());
        unicast.setTicker(message.getTicker());
        unicast.setTitle(message.getTitle());
        unicast.setText(message.getText());
        unicast.goAppAfterOpen();
        unicast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
        // TODO Set 'production_mode' to 'false' if it's a test device.
        // For how to register a test device, please see the developer doc.
        // Set customized fields
        HashMap<String,String> extra = message.getExtra();
        Set<String> set = extra.keySet();
        for (String key:set
             ) {
            unicast.setExtraField(key, extra.get(key));
        }
        unicast.setExtraField("sender",message.getSender());
        unicast.setProductionMode();
//        unicast.setTestMode();
        client.send(unicast);
    }
    public void sendIOSUnicast(Message message) throws Exception {
        IOSUnicast unicast = new IOSUnicast(iosAppKey,iosAppMasterSecret);
        // TODO Set your device token
        LogInfo logInfo = logInfoMapper.selectLogInfoByUser(message.getReceives().get(0));
        if (logInfo==null){
            LOG.info("不存在的设备信息");
            return;
        }
        unicast.setDeviceToken(logInfo.getEquipmentnum());
        HashMap<String,String > map = new HashMap<>();
        map.put("body",message.getText());
        map.put("title",message.getTitle());
        unicast.setAlert(map.toString());
//        unicast.setBadge(0);
        unicast.setSound("default");
        HashMap<String,String> extra = message.getExtra();
        Set<String> set = extra.keySet();
        for (String key:set
                ) {
            unicast.setCustomizedField(key, extra.get(key));
        }
        // TODO set 'production_mode' to 'true' if your app is under production mode
        // Set customized fields
        unicast.setCustomizedField("sender",message.getSender());
        unicast.setTestMode();
//        unicast.setProductionMode();
        client.send(unicast);
    }
    public void sendIOSListCast(Message message,String iosTokens) throws Exception {
        IOSListcast listCast = new IOSListcast(iosAppKey,iosAppMasterSecret);
        // TODO Set your device token
        listCast.setDeviceToken(iosTokens);
        HashMap<String,String > map = new HashMap<>();
        map.put("body",message.getText());
        map.put("title",message.getTitle());
        listCast.setAlert(map.toString());
//        unicast.setBadge(0);
        listCast.setSound("default");
        HashMap<String,String> extra = message.getExtra();
        Set<String> set = extra.keySet();
        for (String key:set
                ) {
            listCast.setCustomizedField(key, extra.get(key));
        }
        // TODO set 'production_mode' to 'true' if your app is under production mode
        // Set customized fields
//        unicast.setCustomizedField("test", "helloworld");
        listCast.setCustomizedField("sender",message.getSender());
        listCast.setTestMode();
//        listCast.setProductionMode();
        client.send(listCast);
    }
    public void sendIOSBroadcast(Message message) throws Exception {
        IOSBroadcast broadcast = new IOSBroadcast(iosAppKey,iosAppMasterSecret);
        HashMap<String,String > map = new HashMap<>();
        map.put("body",message.getText());
        map.put("title",message.getTitle());
        broadcast.setAlert(map.toString());
//        broadcast.setBadge( 0);
        broadcast.setSound("default");
        HashMap<String,String> extra = message.getExtra();
        Set<String> set = extra.keySet();
        for (String key:set
                ) {
            broadcast.setCustomizedField(key, extra.get(key));
        }
        // TODO set 'production_mode' to 'true' if your app is under production mode
        broadcast.setCustomizedField("sender","");
        broadcast.setTestMode();
//        broadcast.setProductionMode();
        // Set customized fields
        String body = broadcast.getPostBody();
        LOG.info("body-----"+body);
        client.send(broadcast);
    }
// 文档内容更新 根据token取信息返回格式       密码一次md5后传入服务器

}
