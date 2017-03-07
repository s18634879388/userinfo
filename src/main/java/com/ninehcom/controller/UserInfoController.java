package com.ninehcom.controller;

import com.ninehcom.entity.LogInfo;
import com.ninehcom.service.UserInfoService;
import com.ninehcom.util.RequestUtils;
import com.ninehcom.util.Result;
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
@RequestMapping(value = "/users")
public class UserInfoController {

    @Autowired
    UserInfoService userService;

//    @Autowired
//    private EditconfigService configService;

    private String appID = "3";

//    @PostConstruct
//    private void init() {
//        appID = configService.getValue(ConfigKeys.AppID);
//    }

    public UserInfoController() {

    }

    /**
     * 新用户的注册功能
     */
    @ApiOperation(value = "手机注册", notes = "新用户的注册", position = 4)
    @ApiResponse(code = 20001, message = "用户数据插入数据库失败")
    @RequestMapping(value = "/register/{mobileNum}/{password}/{checkCode}", method = RequestMethod.POST)
    @ResponseBody
    public Result register(
            HttpServletRequest request,
            @RequestHeader("systemtypeid") String systemtypeid,
            @RequestHeader("equipmentnum") String equipmentnum,
            @PathVariable("mobileNum") String mobileNum,
            @PathVariable("password") String password,
            @PathVariable("checkCode") String checkCode
    ) throws Exception {
        String ip = RequestUtils.getIpAddr(request);
        return userService.register(mobileNum, password, checkCode, appID,
                new LogInfo(ip, systemtypeid, equipmentnum));
    }

    /**
     * 检查手机号是否可用
     */
    @ApiOperation(value = "检查手机号", notes = "检查手机号是否可用", position = 3)
    @RequestMapping(value = "/validatemobile/{mobileNum}", method = RequestMethod.POST)
    @ResponseBody
    public Result validateMobile(
            @ApiParam(value = "电话号码", defaultValue = "15801563000")
            @PathVariable("mobileNum") String mobileNum) throws Exception {
        return userService.validateMobile(mobileNum);
    }

    /**
     * 发送验证码
     */
    @ApiOperation(value = "发送验证码", notes = "发送验证码到指定手机号", position = 1)
    @ApiResponses(value = {
            @ApiResponse(code = 10000, message = "发送失败，短信运营商内部错误"),
            @ApiResponse(code = 10001, message = "sn无效"),
            @ApiResponse(code = 10002, message = "已达单日单号码可发送总条数上限"),
            @ApiResponse(code = 10003, message = "已达单日单平台单号码可发送总条数上限"),
            @ApiResponse(code = 10004, message = "60秒后后可再次对此手机号发送短信")
    })
    @RequestMapping(value = "/sendcode/{mobileNum}", method = RequestMethod.POST)
    @ResponseBody
    public Result sendcode(@ApiParam(value = "电话号码", defaultValue = "15801563000")
                           @PathVariable("mobileNum") String mobileNum) throws Exception {
        return userService.sendCheckCode(mobileNum, appID);
    }
    /**
     * 检验验证码 .
     */
    @ApiOperation(value = "检验验证码", notes = "校验验证码", position = 2)
    @RequestMapping(value = "/validatecode/{mobileNum}/{checkCode}", method = RequestMethod.POST)
    @ResponseBody
    public Result checkCode(
            @ApiParam(value = "电话号码", defaultValue = "15801563000")
            @PathVariable("mobileNum") String mobileNum,
            @ApiParam(value = "短信校验码", defaultValue = "602549")
            @PathVariable("checkCode") String checkCode) throws Exception {
        return userService.checkCode(mobileNum, checkCode, appID);
    }

    /**
     * 国安俱乐部用户中心启动 用户登录
     */
    @ApiOperation(value = "用户登录", notes = "用户登录", position = 5)
    @ApiResponse(code = 20001, message = "用户数据插入数据库失败")
    @RequestMapping(value = "/login/{mobileNum}/{password}", method = RequestMethod.POST)
    @ResponseBody
    public Result login(
            @RequestHeader("systemtypeid") String systemtypeid,
            @RequestHeader("equipmentnum") String equipmentnum,
            @PathVariable("mobileNum") String mobileNum,
            @PathVariable("password") String password,
            HttpServletRequest request) throws Exception {
        String ip = RequestUtils.getIpAddr(request);
        return userService.login(mobileNum, password, appID,
                new LogInfo(ip, systemtypeid, equipmentnum));
    }

    /**
     * 三方登录
     */
    @ApiOperation(value = "三方登录", notes = "三方登录", position = 11)
    @ApiResponse(code = 20001, message = "用户数据插入数据库失败")
    @RequestMapping(value = "/authorizedlogin/{authorizedtypeid}/{openId}", method = RequestMethod.POST)
    @ResponseBody
    public Result threePartLogin(
            @RequestHeader("systemtypeid") String systemtypeid,
            @RequestHeader("equipmentnum") String equipmentnum,
            @PathVariable("openId") String openId,
            @PathVariable("authorizedtypeid") int authorizedtypeid,
            HttpServletRequest request) throws Exception {
        String ip = RequestUtils.getIpAddr(request);
        return userService.threePartLogin(openId, authorizedtypeid, appID,
                new LogInfo(ip, systemtypeid, equipmentnum));
    }

    /**
     * 绑定新手机号
     */
    @ApiOperation(value = "绑定新手机号", notes = "绑定新手机号", position = 12)
    @RequestMapping(value = "/binduser/{mobileNum}/{password}/{checkCode}", method = RequestMethod.POST)
    @ResponseBody
    public Result binduser(
            @RequestHeader("token") String token,
            @PathVariable("mobileNum") String mobileNum,
            @PathVariable("password") String password,
            @PathVariable("checkCode") String checkCode) throws Exception {
        return userService.binduser(token, mobileNum, password, checkCode);
    }
    /**
     * 换绑定手机号
     */
    @ApiOperation(value = "换绑定手机号", notes = "换绑定手机号", position = 6)
    @RequestMapping(value = "/resetmobile/{mobileNum}/{password}/{checkCode}", method = RequestMethod.POST)
    @ResponseBody
    public Result resetMobile(
            @RequestHeader("token") String token,
            @PathVariable("mobileNum") String mobileNum,
            @PathVariable("password") String password,
            @PathVariable("checkCode") String checkCode) throws Exception {
        return userService.resetMobile(token, mobileNum, password, checkCode);
    }
    /**
     * 检查token
     */
    @ApiOperation(value = "检查token", notes = "检查token是否可用", position = 4)
    @ApiResponses(value = {})
    @RequestMapping(value = "/validatetoken", method = RequestMethod.POST)
    @ResponseBody
    public Result validateToken(
            @ApiParam(value = "token", defaultValue = "ade7d1e2-10ab-4481-867b-2e9b78e2061f")
            @RequestHeader("token") String token) throws Exception {
        return userService.validateToken(token);
    }
    /**
     * 用户重置密码
     */
    @ApiOperation(value = "用户重置密码", notes = "用户重置密码", position = 7)
    @RequestMapping(value = "/resetpassword/{mobileNum}/{password}/{checkCode}", method = RequestMethod.POST)
    @ResponseBody
    public Result resetPassword(
            @RequestHeader("systemtypeid") String systemtypeid,
            @RequestHeader("equipmentnum") String equipmentnum,
            @PathVariable("mobileNum") String mobileNum,
            @PathVariable("password") String password,
            @PathVariable("checkCode") String checkCode,
            HttpServletRequest request) throws Exception {
        String ip = RequestUtils.getIpAddr(request);
        return userService.resetPassword(mobileNum, password, checkCode, appID,
                new LogInfo(ip, systemtypeid, equipmentnum));
    }
    /**
     * 用户修改密码
     */
    @ApiOperation(value = "用户修改密码", notes = "用户修改密码", position = 8)
    @RequestMapping(value = "/modifypassword/{oldpassword}/{newpassword}", method = RequestMethod.POST)
    @ResponseBody
    public Result modifyPassword(
            @RequestHeader("token") String token,
            @PathVariable("oldpassword") String oldpassword,
            @PathVariable("newpassword") String newpassword) throws Exception {
        return userService.modifyPassword(token, oldpassword, newpassword);
    }
    /**
     * token 登录
     */
    @ApiOperation(value = "token 登录", notes = "token 登录", position = 13)
    @RequestMapping(value = "/tokenlogin", method = RequestMethod.POST)
    @ResponseBody
    public Result tokenlogin(
            @RequestHeader("token") String token) throws Exception {
        return userService.tokenlogin(token);
    }
    /**
     * 登出
     */
    @ApiOperation(value = "登出", notes = "登出", position = 14)
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ResponseBody
    public Result logout(
            @RequestHeader("token") String token) throws Exception {
        return userService.logout(token);
    }


}
