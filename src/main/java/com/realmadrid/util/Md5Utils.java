package com.realmadrid.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class Md5Utils {
	
	private static  Log log = LogFactory.getLog(Md5Utils.class);

	 public static String md5(String input) {
	        String md5 = null;
	        if(null == input) return null;
	        try {
	            //Create MessageDigest object for MD5
	            MessageDigest digest = MessageDigest.getInstance("MD5");

	            //Update input string in message digest
	            digest.update(input.getBytes(), 0, input.length());

	            byte[] resultByteArray = digest.digest();
	            
	            //Converts message digest value in base 16 (hex)
	            md5 = byteArrayToHex(resultByteArray);
	            md5 = md5.toLowerCase();

	        } catch (NoSuchAlgorithmException e) {
	        	log.error(e);
	        }
	        return md5;
	    }
	 
	 public static String byteArrayToHex(byte[] byteArray) {

		  // 首先初始化一个字符数组，用来存放每个16进制字符

		  char[] hexDigits = {'0','1','2','3','4','5','6','7','8','9', 'A','B','C','D','E','F' };



		  // new一个字符数组，这个就是用来组成结果字符串的（解释一下：一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方））

		  char[] resultCharArray =new char[byteArray.length * 2];



		  // 遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去

		  int index = 0;

		  for (byte b : byteArray) {

		     resultCharArray[index++] = hexDigits[b>>> 4 & 0xf];

		     resultCharArray[index++] = hexDigits[b& 0xf];

		  }



		  // 字符数组组合成字符串返回

		  return new String(resultCharArray);
		}
	 
	 public static String md5Old(String input) {
	        String md5 = null;
	        if(null == input) return null;
	        try {
	            //Create MessageDigest object for MD5
	            MessageDigest digest = MessageDigest.getInstance("MD5");

	            //Update input string in message digest
	            digest.update(input.getBytes(), 0, input.length());

	            //Converts message digest value in base 16 (hex)
	            md5 = new BigInteger(1, digest.digest()).toString(16);

	        } catch (NoSuchAlgorithmException e) {
	        	log.error(e);
	        }
	        return md5;
	    }

	/**
	 * 生成盐
	 * @return
	 */
	public static String getNextSalt(){

		return getRamdomIntString(6);
	}

	public static String getRamdomIntString(int length){
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
}
