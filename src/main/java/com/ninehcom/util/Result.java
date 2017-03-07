package com.ninehcom.util;

import com.ninehcom.util.ErrorCode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;

/**
 * 结果对象 success boolean	是否成功 errCode	int	错误码（0：成功） message	String	消息
 */
public class Result implements Serializable {

    public static final int SuccessCode = 0x00000000;
    public static final int UnknownCode = 0xffffffff;

    private int errCode;
    private String message;
    private Serializable tag;

    public String getValue(String key) {
        HashMap<String, String> map = (HashMap<String, String>) tag;
        if (map != null && map.containsKey(key)) {
            return map.get(key);
        } else {
            return null;
        }
    }

    public static Result getResult(String response) throws JSONException {
        JSONObject obj = new JSONObject(response);
        return getResult(obj);
    }

    public static Result getResult(JSONObject obj) throws JSONException {
        HashMap<String, String> map = new HashMap<>();
        int code = obj.getInt("code");
        String message;
        try {
            message = obj.getString("msg");
        } catch (JSONException ex) {
            message = "";
        }
        Iterator keys = obj.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            if (!key.equals("code") && !key.equals("msg")) {
                map.put(key, obj.getString(key));
            }
        }

        return new Result(code, message, map);
    }

    public Result() {
    }

    public Result(int errCode) {
        this(errCode, "", null);
    }

    public Result(int errCode, String message) {
        this(errCode, message, null);
    }

    public Result(int errCode, String message, Serializable tag) {
        this.errCode = errCode;
        this.message = message;
        this.tag = tag;
    }
    
    public Result(ErrorCode code, Serializable tag) {
        this.errCode = code.getCode();
        this.message = code.getMessage();
        this.tag = tag;
    }

    public static Result Success(String message, Serializable tag) {
        return new Result(SuccessCode, message, tag);
    }

    public static Result Success() {
        return Success(ErrorCode.Success.getMessage(), null);
    }

    public static Result Success(String message) {
        return Success(message, null);
    }
    
    public static Result Success(Serializable tag) {
        return Success(ErrorCode.Success.getMessage(), tag);
    }

    public static Result Fail(int errCode, String message, Throwable ex) {
        return new Result(errCode, message, ex);
    }

    public static Result Fail(int errCode, String message) {
        return Fail(errCode, message, null);
    }

    public static Result Fail(int errCode) {
        return Fail(errCode, "", null);
    }

    public static Result Fail(ErrorCode code) {
        return Fail(code.getCode(), code.getMessage());
    }
    
    public static Result Fail(ErrorCode code,Throwable ex) {
        return Fail(code.getCode(), code.getMessage(),ex);
    }

    public static Result Result(ErrorCode code,Serializable tag) {
        return new Result(code.getCode(), code.getMessage(),tag);
    }

    public boolean isSuccess() {
        return errCode == SuccessCode;
    }

    /**
     * @return the errCode
     */
    public int getErrCode() {
        return errCode;
    }

    /**
     * @param errCode the errCode to set
     */
    public void setErrCode(int errCode) {
        this.errCode = errCode;
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

    /**
     * @return the tag
     */
    public Serializable getTag() {
        return tag;
    }

    /**
     * @param tag the tag to set
     */
    public void setTag(Serializable tag) {
        this.tag = tag;
    }


}
