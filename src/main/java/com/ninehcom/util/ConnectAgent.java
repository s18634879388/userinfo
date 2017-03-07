/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ninehcom.util;

import org.springframework.stereotype.Component;

/**
 *
 * @author Shenjizhe
 */
@Component
public class ConnectAgent {
    public String sendRequestHttpsUTF8(String request) throws Exception{
        return HttpsUtil.getAsString(request, "utf-8");
    }
    
//    public String sendRequestHttpUTF8(String request) throws Exception{
//        return HttpUtils.getAsString(request, "utf-8",null);
//    }
}
