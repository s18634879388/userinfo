package com.ninehcom.util;

import java.util.Random;
import java.util.Stack;

public class Base62Utils {

    private static char[] charSet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

    public static String getRamdomIntString(int length) {
        // 创建一个随机数生成器类。   
        Random random = new Random();
        // randomCode用于保存随机产生的验证码，以便用户登录后进行验证。   
        StringBuffer randomCode = new StringBuffer();
		// 设置默认生成6个验证码   

        // 设置备选验证码:包括"a-z"和数字"0-9"   
        String base = "0123456789";

        int size = base.length();

        // 随机产生4位数字的验证码。   
        for (int i = 0; i < length; i++) {
            // 得到随机产生的验证码数字。   
            int start = random.nextInt(size);
            String strRand = base.substring(start, start + 1);
            // 将产生的四个随机数组合在一起。   
            randomCode.append(strRand);
        }
        return randomCode.toString();
    }

    public static String getNextAccount() {
        String str = System.currentTimeMillis() + getRamdomIntString(3);
        //System.out.println(str);
        return Base62Utils._10_to_62(Long.parseLong(str), 11);
    }

    /**
     * 将10进制转化为62进制
     *
     * @param number
     * @param length 转化成的62进制长度，不足length长度的话高位补0，否则不改变什么
     * @return
     */
    public static String _10_to_62(long number, int length) {
        Long rest = number;
        Stack<Character> stack = new Stack<Character>();
        StringBuilder result = new StringBuilder(0);
        while (rest != 0) {
            stack.add(charSet[new Long((rest - (rest / 62) * 62)).intValue()]);
            rest = rest / 62;
        }
        for (; !stack.isEmpty();) {
            result.append(stack.pop());
        }
        int result_length = result.length();
        StringBuilder temp0 = new StringBuilder();
        for (int i = 0; i < length - result_length; i++) {
            temp0.append('0');
        }

        return temp0.toString() + result.toString();

    }

    /**
     * 将62进制转换成10进制数
     *
     * @param ident62
     * @return
     */
    private static String convertBase62ToDecimal(String ident62) {
        int decimal = 0;
        int base = 62;
        int keisu = 0;
        int cnt = 0;

        byte ident[] = ident62.getBytes();
        for (int i = ident.length - 1; i >= 0; i--) {
            int num = 0;
            if (ident[i] > 48 && ident[i] <= 57) {
                num = ident[i] - 48;
            } else if (ident[i] >= 65 && ident[i] <= 90) {
                num = ident[i] - 65 + 10;
            } else if (ident[i] >= 97 && ident[i] <= 122) {
                num = ident[i] - 97 + 10 + 26;
            }
            keisu = (int) Math.pow((double) base, (double) cnt);
            decimal += num * keisu;
            cnt++;
        }
        return String.format("%08d", decimal);
    }
}
