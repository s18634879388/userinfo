/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realmadrid.util;

import javax.servlet.http.HttpServletRequest;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class RequestUtils {

    /**
     * 获取用户的客户端ip ip = request.getRemoteAddr();
     *
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {

        LOG.info("===wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww==");
        LOG.info("X-Real-IP:" + request.getHeader("X-Real-IP"));
        LOG.info("x-forwarded-for:" + request.getHeader("x-forwarded-for"));
        LOG.info("Proxy-Client-IP:" + request.getHeader("Proxy-Client-IP"));
        LOG.info("WL-Proxy-Client-IP:" + request.getHeader("WL-Proxy-Client-IP"));

        String ip = request.getHeader("X-Real-IP");
        if (ip == null || ip.length() == 0 || " unknown ".equalsIgnoreCase(ip)) {
            ip = request.getHeader("x-forwarded-for");
        }
        if (ip == null || ip.length() == 0 || " unknown ".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || " unknown ".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || " unknown ".equalsIgnoreCase(ip)) {
        }

        return ip;
    }
    private static final Logger LOG = Logger.getLogger(RequestUtils.class.getName());
}
