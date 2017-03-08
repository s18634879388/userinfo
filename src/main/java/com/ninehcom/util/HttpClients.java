package com.ninehcom.util;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by Administrator on 2017/2/23.
 */
public class HttpClients {
    public static String get(String url){
        CloseableHttpClient httpClient = org.apache.http.impl.client.HttpClients.createDefault();
        String jsonStr = null;
        HttpGet httpGet = new HttpGet(url);
        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);
            try {
                HttpEntity entity = response.getEntity();
                System.out.println(response.getStatusLine()+"----------------");
                if (entity!=null){
                    jsonStr = EntityUtils.toString(entity);
                }
            } finally {
                response.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return jsonStr;
    }

    public static String post(String url,String data){
        CloseableHttpClient httpClient = org.apache.http.impl.client.HttpClients.createDefault();
        String jsonStr = null;

        try {
            HttpPost httpPost = new HttpPost(url);
            HttpEntity send_entity = new StringEntity(data);
            httpPost.setEntity(send_entity);
            httpPost.setHeader("Content-Type", "application/json");
            CloseableHttpResponse response = httpClient.execute(httpPost);

            try {
                HttpEntity entity = response.getEntity();
                System.out.println(response.getStatusLine()+"-------");
                if (entity!=null){
                    jsonStr = EntityUtils.toString(entity);
                }
            } finally {
                response.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return jsonStr;
    }

}
