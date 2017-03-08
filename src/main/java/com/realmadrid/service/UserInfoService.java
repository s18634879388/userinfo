package com.realmadrid.service;

import com.realmadrid.entity.LogInfo;
import com.realmadrid.entity.UserInfo;
import com.realmadrid.mapper.UserInfoMapper;
import com.realmadrid.util.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
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
            userInfo.setPassword(Md5Utils.md5(password));
            userInfo.setNickName(nickname);
            userInfo.setPhoneNumber(mobileNum);
            int ret = userInfoMapper.insertUser(userInfo);
            if (ret != 1) {
                return Result.Fail(ErrorCode.UserInsertDBFail);
            }
        }
        return result;
    }


    /**
     * 发送验证码
     */
    public Result sendCheckCode(String mobilenum, String appid) throws JSONException {
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
                userInfo.setPassword(Md5Utils.md5(password));
                int ret = userInfoMapper.insertUser(userInfo);
                if (ret != 1) {
                    return Result.Fail(ErrorCode.UserInsertDBFail);
                }
            }else {
                user.setPassword(Md5Utils.md5(password));
                userInfoMapper.updateUserPass(user);
            }
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
                user.setId(UUID.randomUUID().toString());
                user.setUnionId(unionId);
                int ret = userInfoMapper.insertUser(user);
                if (ret != 1) {
                    return Result.Fail(ErrorCode.UserInsertDBFail);
                }
//                user = new UserInfo();
//                user.setId(userId);
//                user.setNickName(nickname);
//                try {
//                    boolean flag = searchAgent.updateSearchWord(user);
//                    if (!flag){
//                        return Result.Fail(ErrorCode.SearchAgentUpdateFail);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
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
        } catch (Exception ex) {
            return Result.Fail(ErrorCode.UserCenterCantConnect, ex);
        }
        return Result.getResult(response);
    }
    /**
     * 用户重置密码
     */
    public Result resetPassword(String mobileNum, String password, String checkCode, String appID, LogInfo logInfo) throws JSONException {
        String response = null;
        try {
            response = ucAgent.resetPassword(mobileNum, password, checkCode, appID, logInfo);
        } catch (Exception ex) {
            return Result.Fail(ErrorCode.UserCenterCantConnect, ex);
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
                user.setPhoneNumber(mobileNum);
                result.setTag(user);
            }
        }
        return result;
    }


}
