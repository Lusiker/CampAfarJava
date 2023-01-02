package com.campfire.campafar.Utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
public class StringEncoder {
    private static final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(10);

    /**
     * userPasswordEncode for encoding user password before inserting into the database
     * @param userPassword, passed in from register module
     * @return encoded user password
     */
    public static String encode(String userPassword){
        return bCryptPasswordEncoder.encode(userPassword);
    }

    /**
     * @param newPassword: password entered by user
     * @param SavedPassword: password that has been encoded and stored in the database
     * @return match result boolean
     */
    public static boolean match(String newPassword, String SavedPassword){
        return bCryptPasswordEncoder.matches(newPassword,SavedPassword);
    }
}