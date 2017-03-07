package com.ninehcom.service;

import com.ninehcom.entity.LogInfo;
import com.ninehcom.entity.UserInfo;
import com.ninehcom.mapper.UserInfoMapper;
import com.ninehcom.util.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * UserUserinfo的Service
 *
 * @author shenjizhe
 * @version 1.0.0
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
    ) throws JSONException {

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
            int ret = insertUser(userId, nickname);
            if (ret != 1) {
                return Result.Fail(ErrorCode.UserInsertDBFail);
            }
            UserInfo userInfo = new UserInfo();
            userInfo.setId(userId);
            userInfo.setNickName(nickname);
//            try {
//                boolean flag = searchAgent.updateSearchWord(userInfo);
//                if (!flag){
//                    return Result.Fail(ErrorCode.SearchAgentUpdateFail);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }
        return result;
    }

    private int insertUser(String userId, String nickName) {
        int ret = userInfoMapper.insertUser(userId, nickName);
//        Level level = levelService.getLevel(0);
//        userStatisticsMapper.insertUserStatistics(userId, level.getId(), level.getTitle());
        return ret;
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
        ResSuccess(result);

        return result;
    }
    public Result ResSuccess(Result result){
        if (result.isSuccess()) {
            String userId = result.getValue(UCAgent.KEY_USER_ID);
            if (userId == null || userId.isEmpty()) {
                return Result.Fail(ErrorCode.UserIdIsEmpty);
            }
            UserInfo user = userInfoMapper.selectUserInfoById(userId);
            if (user == null) {
                String nickname = Base62Utils.getNextAccount();
                int ret = insertUser(userId, nickname);
                if (ret != 1) {
                    return Result.Fail(ErrorCode.UserInsertDBFail);
                }
                user = new UserInfo();
                user.setId(userId);
                user.setNickName(nickname);
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
     * 三方登录
     */
    public Result threePartLogin(String openId, int authorizedtypeid, String appId, LogInfo logInfo) throws JSONException {

        String response = null;
        try {
            response = ucAgent.threePartLogin(openId, authorizedtypeid, appId, logInfo);
        } catch (Exception ex) {
            return Result.Fail(ErrorCode.UserCenterCantConnect, ex);
        }

        Result result = Result.getResult(response);
        ResSuccess(result);
        return result;
    }
    /**
     * 绑定新手机号
     */
    public Result binduser(String token, String mobileNum, String password, String checkCode) throws JSONException {

        String response = null;
        try {
            response = ucAgent.binduser(token, mobileNum, password, checkCode);
        } catch (Exception ex) {
            return Result.Fail(ErrorCode.UserCenterCantConnect, ex);
        }
        return Result.getResult(response);
    }
    /**
     * 换绑定手机号
     */
    public Result resetMobile(String token, String mobileNum, String password, String checkCode) throws JSONException {
        String response = null;
        try {
            response = ucAgent.resetMobile(token, mobileNum, password, checkCode);
        } catch (Exception ex) {
            return Result.Fail(ErrorCode.UserCenterCantConnect, ex);
        }
        return Result.getResult(response);
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
     * 用户修改密码
     */
    public Result modifyPassword(String token, String oldpassword, String newpassword) throws JSONException {

        String response = null;
        try {
            response = ucAgent.modifyPassword(token, oldpassword, newpassword);
        } catch (Exception ex) {
            return Result.Fail(ErrorCode.UserCenterCantConnect, ex);
        }
        return Result.getResult(response);
    }
    public Result tokenlogin(String token) throws JSONException {

        String response = null;
        try {
            response = ucAgent.tokenlogin(token);
        } catch (Exception ex) {
            return Result.Fail(ErrorCode.UserCenterCantConnect, ex);
        }

        Result result = Result.getResult(response);
        ResSuccess(result);
        return result;
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

}
