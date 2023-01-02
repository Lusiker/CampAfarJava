package com.campfire.campafar.Utils;

import java.util.Date;
import java.util.Random;

public class TempInfoGenerator {
    private static final Random random = new Random();

    /**
     * 由于用户使用邮箱进行注册，注册时应为其生成一个随机的用户名，此生成基于用户邮箱与当前时间戳
     * @param now: 注册时的时间戳
     * @param newUserEmail: 注册时用户使用的邮箱
     * @return 生成的随机用户名
     */
    public static String generateTempUsername(Date now, String newUserEmail){
        String userNameTemp = Long.toString(now.getTime());
        int charSum = 0;
        for(char c : newUserEmail.toCharArray()){
            charSum += c;
        }
        userNameTemp += Integer.toString(charSum);
        int len = userNameTemp.length();
        userNameTemp = userNameTemp.substring(len / 2 - 3, len);
        // user's initial username is generated from the time the account is created
        // and the given email address

        return "user_" + userNameTemp;
    }

    /**
     * Function for generating a String-typed 4-digit number
     * @param seed - a long number from timestamp
     * @return generated 4-digit number
     */
    public static String getRandomFourDigitNumber(long seed) {
        random.setSeed(seed);

        int newNum = random.nextInt(1, 10000);
        StringBuilder code = new StringBuilder(Integer.toString(newNum));
        while (newNum <= 10000) {
            newNum *= 10;
            if (newNum >= 10000) {
                break;
            } else {
                code.insert(0, "0");
            }
        }

        return code.toString();
    }
}
