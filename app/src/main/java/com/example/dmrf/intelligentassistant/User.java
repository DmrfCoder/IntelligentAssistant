package com.example.dmrf.intelligentassistant;

/**
 * Created by DMRF on 2017/8/8.
 */

public class User {
    private static String UserName;

    public static String getUserName() {
        return UserName;
    }

    public static void setUserName(String userName) {
        UserName = userName;
    }
}
