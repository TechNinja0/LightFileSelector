package com.tea.fileselectlibrary.utils;



public class DoubleUtils {
    private static long lastClickTime;
    private final static long TIME = 500;
    private final static long TIME_SHORT = 300;

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < TIME) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
    public static boolean isFastDoubleClickShort() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < TIME_SHORT) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
