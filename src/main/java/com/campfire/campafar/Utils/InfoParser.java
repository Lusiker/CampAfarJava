package com.campfire.campafar.Utils;

/**
 * InfoParser类存放与parsing相关的方法
 * 在系统的主要位置，应当使用封装的基本数据类型以方便用判空方式处理错误
 */
public class InfoParser {
    /**
     * 将字符串转换为整数
     * @param intStr： 输入的字符串
     * @return result：转化后的整数
     */
    public static Integer parseInt(String intStr){
        int result;

        try{
            result = Integer.parseInt(intStr);
        }catch (Exception e){
            return null;
        }

        return result;
    }

    /**
     * 将字符串转换为short
     * @param shortStr： 输入的字符串
     * @return result：转化后的short型
     */
    public static Short parseShort(String shortStr){
        short result;

        try{
            result = Short.parseShort(shortStr);
        }catch (Exception e){
            return null;
        }

        return result;
    }

    /**
     * 将字符串转换为bool
     * @param boolStr： 输入的字符串
     * @return result：转化后的bool值
     */
    public static Boolean parseBoolean(String boolStr){
        boolean result;

        try{
            result = Boolean.parseBoolean(boolStr);
        }catch (Exception e){
            return null;
        }

        return result;
    }
}
