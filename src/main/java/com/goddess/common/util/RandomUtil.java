package com.goddess.common.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;


/**
 * 处理各种随机字符串或数值
 *
 */
public class RandomUtil {
	/**
	 * 生成随机字符串
	 * @strLength   生成字符串长度  strLength必须大于0
	 * @return
	 */
	public static String createNonceStr(String chars,int strLength) {
		String res = "";
		for (int i = 0; i < strLength; i++) {
			Random rd = new Random();
			res += chars.charAt(rd.nextInt(chars.length() - 1));
		}
		return res;
	}
	/**
	 * 生成随机字符串
	 * @strLength   生成字符串长度  strLength必须大于0
	 * @return
	 */
	public static String createNonceStr(int strLength) {
		String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		String res = "";
		for (int i = 0; i < strLength; i++) {
			Random rd = new Random();
			res += chars.charAt(rd.nextInt(chars.length() - 1));
		}
		return res;
	}
	/**
	 * @Title: getUUID 
	 * @Description: JAVA生成36进制序列号
	 * @param @return    设定文件 
	 * @return String    返回类型 	 
	 * @date 2011-10-8 下午05:55:15 
	 * @throws
	 */
	public static String getUUID()
	{ 
        String s = UUID.randomUUID().toString().toUpperCase();
        return s.substring(0,8)+s.substring(9,13)+s.substring(14,18)+s.substring(19,23)+s.substring(24);
    } 
	
	/**
	 * 唯一序列
	 * 由当前时间（yyyyMMddHHmmssSSS）
	 * @return
	 */
     public static String getCurrentDateSequen()
     {
    	 //时间格式
    	 SimpleDateFormat tempDate = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    	 //生成时间字符串
 		 String cdtSequen = tempDate.format(new java.util.Date());
 		 //生成结果字符串
 		 String rtnStr = cdtSequen;
    	 return rtnStr;
     }
     
     /**
      * 随机函数
      * 生成随机length位数的字符串
      * @param length
      * @return
      */
     public static String getRandomNumber(int length){
    	 int val = (int)Math.pow(10, (length+1));
    	 int rdm = new Random().nextInt(val);
    	 String rs = "" +rdm;
    	 if(rs.length()>length){
    		 rs = String.valueOf(rdm).substring(0, length);
    	 }else{
    		 rs =String.format("%05d",String.valueOf(rdm));
    	 }
    	 return rs;
     }


     /**
      * 返回随机数列表，最小值从0开始
      * @param max  总个数
      * @param count 取数
      * @return
      */
     public static List<Integer> getRandom(int max,int count){
    	 if(count>max)
 		{
 			throw new IllegalArgumentException("count must be < max");
 		}
 		ArrayList<Integer> list = new ArrayList<Integer>();
         for (int i = 0; i < max; i++) {
             list.add(i + 1);
         }
         ArrayList<Integer> rs = new ArrayList<Integer>();
         int temp = max;
         for (int i = 0; i < max; i++) {
             int randomInt = new Random().nextInt(temp);
             rs.add(list.get(randomInt)-1);
             list.remove(randomInt);
             temp--;           
         }
         return rs.subList(0, count);
     }
     
     public static Integer getFromMinToMaxOneNum(int min,int max) {
		 Random random = new Random();

		 return random.nextInt(max)%(max-min+1) + min;
	}
}
