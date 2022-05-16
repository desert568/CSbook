package com.kinghis.emri.config;

public enum UserEnum {

    TXG("txg", "1"),
    MWD("mwd", "2"),
    YLS("yls", "3");

    private final String code;
    private final String name;

    UserEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getName(String code) {
        for (UserEnum c : UserEnum.values()) {
            if (code.trim().equals(c.code)) {
                return c.name;
            }
        }
        return "";
    }

}
