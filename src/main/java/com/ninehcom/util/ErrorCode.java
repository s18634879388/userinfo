/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ninehcom.util;

/**
 *
 * @author Shenjizhe
 */
public enum ErrorCode {

    Success(0, "成功"),
    Fail(1, "失败"),
    SMSCantConnect(10, "短信中心连接失败"),
    UserCenterCantConnect(11, "用户中心连接失败"),
    SensitiveWordCantConnect(12, "敏感词服务连接失败"),
    // 短信中心错误码
    //    SMSProviderFail(10000, "发送失败，短信运营商内部错误"),
    //    SMSNFail(10001, "sn无效"),
    //    SMNumberLimitFail(10002, "已达单日单号码可发送总条数上限"),
    //    SMPlatformLimitFail(10003, "已达单日单平台单号码可发送总条数上限"),
    //    SMIntevalFail(10004, "60秒后后可再次对此手机号发送短信"),
    // 用户接口错误码
    UserInsertDBFail(20001, "用户数据插入数据库失败"),
    UserUpdateDBFail(20002, "用户数据更新数据库失败"),
    VersionTypeNotExistFail(20004, "当前类型不存在"),
    VersionUploadURLNotFound(20005, "系统中没有配置版本升级路径"),
    VersionFormatFail(20006, "版本格式错误"),
    UserNotExist(20007, "用户不存在"),
    NickNameUsed(20008, "用户昵称已被使用"),
    SearchAgentUpdateFail(20009,"搜索引擎更新失败"),
    // 用户中心错误码
    UserIdIsEmpty(20100, "用户ID为空"),
    UserSignedInToday(20101, "用户今天已经签到"),
    UserSignedFail(20102, "用户签到数据添加失败"),
    UserSigneSearchFail(20103, "用户签到排名数据添加失败"),
    UserSigneSearchUpdateFail(20104, "用户签到信息更新失败"),
    UserUnSigned(20105, "用户不存在或者从未签到"),
    UserNickNameAlreadyChanged(20106, "昵称修改次数超限"),
    UserUnSignedInToday(20107, "用户当天未签到"),
    NickNameLimitConfigEmpty(20108, "昵称限制次数配置为空"),
    GetFeedbackFail(20109, "取得用户反馈失败"),
    SendFeedbackFail(20110, "发送用户反馈失败"),
    ReviewFeedbackFail(20111, "审阅用户反馈失败"),
    ScoreAddToDBFail(20112, "添加积分数据到数据库失败"),
    ScoreSubToDBFail(20113, "扣除积分数据到数据库失败"),
    NoScoreForSubFail(20114, "用户当天没有积分可扣除"),
    ScoreValidateFail(20115, "积分或者经验不能为负值"),
    AttentionPlayerDBFail(20116, "关注球员数据库更新失败"),
    CancelAttentionPlayerDBFail(20117, "取消关注球员数据库更新失败"),
    PlayerAttentioned(20118, "球员已经关注"),
    PlayerUnAttentioned(20119, "球员还未关注"),
    ConfigKeyNotContains(20120,"配置项不存在"),
    SignDateTooEarly(20150, "签到时间早于系统上线时间"),
    SignDateTooLater(20151,"补签时间不能早于当前时间"),
    SignDateregisterTime(20152,"补签时间早于注册时间"),
    UserSigned(20153,"用户已经签过到"),
    UserAddSignedFail(20154, "用户补签数据添加失败,请查看错误日志,手动添加补签卡"),
    UserAddSignedLogFail(20155,"用户补签失败,并且错误日志记录也失败"),
    NickSensitiveWordContains(20201, "昵称含有敏感词"),
    SignatureNickSensitiveWordContains(20202, "个性签名含有敏感词"),
    RequestWrongFormat(30001, "请求格式错误"),
    ServiceOldVersionWrongFormat(30101, "服务器配置的老版本格式不正确"),
    ServiceNewVersionWrongFormat(30102, "服务器配置的最新版本格式不正确"),
    ClientVersionWrongFormat(30103, "客户端配置的版本格式不正确"),
    UserHaveNotRight(30201,"用户没有操作权限"),
    UserSigning(30202,"用户正在签到中");
    private int code;
    private String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
