package com.realmadrid.util;

/**
 * Created by Administrator on 2017/8/3.
 */
public class NinehStringUtils {
    private static String NULL_STRING = "";
    public static String format(String str, Object... args){
        for(int i = 0; i < args.length; i++){
            if(args[i] == null){
                args[i] = NULL_STRING;
            }
        }

        return String.format(str, args);
    }
}
