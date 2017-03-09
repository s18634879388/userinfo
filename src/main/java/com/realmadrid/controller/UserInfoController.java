package com.realmadrid.controller;

import com.realmadrid.entity.LogInfo;
import com.realmadrid.entity.UserInfo;
import com.realmadrid.service.UserInfoService;
import com.realmadrid.util.RequestUtils;
import com.realmadrid.util.Result;
import com.wordnik.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


/**
 * UserInfo 的控制器，用于显示同时查询2个数据库的结果 * @author shenjizhe
 *
 * @version 1.0.0
 */
@Api(basePath = "/users", value = "发送验证码", description = "验证码", produces = "application/json")
@RestController
@RequestMapping(value = "/users/v1")
public class UserInfoController {

    @Autowired
    UserInfoService userService;

    private String appID = "8";


    public UserInfoController() {

    }

    /**
     * 新用户的注册功能
     */
    @ApiOperation(value = "应用账号注册(手机号)", notes = "应用账号注册(手机号)", position = 4)
    @ApiResponse(code = 20001, message = "用户数据插入数据库失败")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public Result register(
            HttpServletRequest request,
            @RequestHeader("systemtypeid") String systemtypeid,
            @RequestHeader("equipmentnum") String equipmentnum,
            @RequestBody UserInfo userInfo
            ) throws Exception {
        String ip = RequestUtils.getIpAddr(request);
        return userService.register(userInfo.getPhoneNumber(), userInfo.getPassword(), userInfo.getCheckCode(), appID,
                new LogInfo(ip, systemtypeid, equipmentnum));
    }

//    /**
//     * 检查手机号是否可用
//     */
//    @ApiOperation(value = "检查手机号", notes = "检查手机号是否可用", position = 3)
//    @RequestMapping(value = "/validatemobile/{mobileNum}", method = RequestMethod.POST)
//    @ResponseBody
//    public Result validateMobile(
//            @ApiParam(value = "电话号码", defaultValue = "15801563000")
//            @PathVariable("mobileNum") String mobileNum) throws Exception {
//        return userService.validateMobile(mobileNum);
//    }

    /**
     * 发送验证码
     */
    @ApiOperation(value = "手机发送验证码", notes = "手机发送验证码", position = 1)
    @ApiResponses(value = {
            @ApiResponse(code = 10000, message = "发送失败，短信运营商内部错误"),
            @ApiResponse(code = 10001, message = "sn无效"),
            @ApiResponse(code = 10002, message = "已达单日单号码可发送总条数上限"),
            @ApiResponse(code = 10003, message = "已达单日单平台单号码可发送总条数上限"),
            @ApiResponse(code = 10004, message = "60秒后后可再次对此手机号发送短信")
    })
    @RequestMapping(value = "/sendcode", method = RequestMethod.POST)
    @ResponseBody
    public Result sendcode(
                           @RequestBody UserInfo userInfo) throws Exception {
        return userService.sendCheckCode(userInfo.getPhoneNumber(),userInfo.getStage(), appID);
    }
    /**
     * 检验验证码 .
     */
    @ApiOperation(value = "[注册/重置密码]前检查验证码是否正确", notes = "[注册/重置密码]前检查验证码是否正确", position = 2)
    @RequestMapping(value = "/validatecode", method = RequestMethod.POST)
    @ResponseBody
    public Result checkCode(
            @RequestBody UserInfo userInfo) throws Exception {
        return userService.checkCode(userInfo.getPhoneNumber(), userInfo.getCheckCode(), appID);
    }

    /**
     * 国安俱乐部用户中心启动 用户登录
     */
    @ApiOperation(value = "应用账号登录(手机号)", notes = "应用账号登录(手机号)", position = 5)
    @ApiResponse(code = 20001, message = "用户数据插入数据库失败")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public Result login(
            @RequestHeader("systemtypeid") String systemtypeid,
            @RequestHeader("equipmentnum") String equipmentnum,
            @RequestBody UserInfo userInfo,
            HttpServletRequest request) throws Exception {

        //token登录何时需要   token的生成规则，何时过期，过期销毁？
        String ip = RequestUtils.getIpAddr(request);
        return userService.login(userInfo.getPhoneNumber(), userInfo.getPassword(), appID,
                new LogInfo(ip, systemtypeid, equipmentnum));
    }

    /**
     * 三方登录
     */
    @ApiOperation(value = "微信账号[注册/登录] (有该账号则登录，没有则创建，返回时下发User对象，并更新token)", notes = "微信账号[注册/登录] (有该账号则登录，没有则创建，返回时下发User对象，并更新token)", position = 11)
    @ApiResponse(code = 20001, message = "用户数据插入数据库失败")
    @RequestMapping(value = "/login-wx", method = RequestMethod.POST)
    @ResponseBody
    public Result threePartLoginv2(
            @RequestHeader("systemtypeid") String systemtypeid,
            @RequestHeader("equipmentnum") String equipmentnum,
            @RequestBody UserInfo userInfo,
            HttpServletRequest request) throws Exception {
        String ip = RequestUtils.getIpAddr(request);
        return userService.threePartLoginv2(userInfo.getOpenid(), userInfo.getUnionId(), userInfo.getAccesstoken(),
                userInfo.getAccesstoken(), userInfo.getAuthorizedtypeid(), appID,userInfo.getNickName(),userInfo.getHeadimgurl(),
                new LogInfo(ip, systemtypeid, equipmentnum));
    }

    /**
     * 检查token
     */
    @ApiOperation(value = "客户端检查当前token有效性", notes = "客户端检查当前token有效性", position = 4)
    @ApiResponses(value = {})
    @RequestMapping(value = "/tokens/{accesstoken}", method = RequestMethod.GET)
    @ResponseBody
    public Result validateToken(@PathVariable("accesstoken") String accesstoken) throws Exception {
        return userService.validateToken(accesstoken);
    }
    /**
     * 用户重置密码
     */
    @ApiOperation(value = "用户重置密码(需要步骤4完成)", notes = "用户重置密码(需要步骤4完成)", position = 7)
    @RequestMapping(value = "/{mobileNum}", method = RequestMethod.PUT)
    @ResponseBody
    public Result resetPassword(
            @RequestHeader("systemtypeid") String systemtypeid,
            @RequestHeader("equipmentnum") String equipmentnum,
            @PathVariable("mobileNum") String mobileNum,
            @RequestBody UserInfo userInfo,
            HttpServletRequest request) throws Exception {
        String ip = RequestUtils.getIpAddr(request);
        return userService.resetPassword(mobileNum, userInfo.getPassword(), userInfo.getCheckCode(), appID,
                new LogInfo(ip, systemtypeid, equipmentnum));
    }

//    /**
//     * 登出
//     */
//    @ApiOperation(value = "登出", notes = "登出", position = 14)
//    @RequestMapping(value = "/logout", method = RequestMethod.POST)
//    @ResponseBody
//    public Result logout(
//            @RequestHeader("token") String token) throws Exception {
//        return userService.logout(token);
//    }
    /**
     * 根据token取得用户信息
     */
    @ApiOperation(value = "根据token取得用户信息", notes = "根据token取得用户信息", position = 9)
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    @ResponseBody
    public Result getUserbytoken(
            @RequestParam(value = "accesstoken") String token) throws Exception {
        return userService.getUserbytoken(token);
    }



}
