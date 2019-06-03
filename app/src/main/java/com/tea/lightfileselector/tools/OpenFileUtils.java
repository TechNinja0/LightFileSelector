package com.tea.lightfileselector.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Environment;
import android.os.UserHandle;
import android.text.TextUtils;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OpenFileUtils {

    /**
     * 判断Intent 是否存在 防止崩溃
     *
     * @param context 上下文
     * @param intent 意图
     * @return 是否存在
     */
    public static boolean isIntentAvailable(Context context, Intent intent) {
        final PackageManager packageManager = context.getPackageManager();
        @SuppressLint("WrongConstant") List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
                PackageManager.GET_ACTIVITIES);
        return list.size() > 0;
    }

    public static String getSDCardPath6() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //===========================获取UserEnvironment================
                Class<?> userEnvironment = Class.forName("android.os.Environment$UserEnvironment");
                Method getExternalDirs = userEnvironment.getDeclaredMethod("getExternalDirs");
                getExternalDirs.setAccessible(true);
                //========获取构造UserEnvironment的必要参数UserId================
                Class<?> userHandle = Class.forName("android.os.UserHandle");
                Method myUserId = userHandle.getDeclaredMethod("myUserId");
                myUserId.setAccessible(true);
                int mUserId = (int) myUserId.invoke(UserHandle.class);
                Constructor<?> declaredConstructor = userEnvironment.getDeclaredConstructor(Integer.TYPE);
                // 得到UserEnvironment instance
                Object instance = declaredConstructor.newInstance(mUserId);
                File[] files = (File[]) getExternalDirs.invoke(instance);
                for (File file : files) {
                    if (Environment.isExternalStorageRemovable(file)) {
                        return file.getPath();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * 获取外置SD卡路径
     *
     * @return 外置sd卡路径
     */
    public static String getSDCardPath5() {
        String exterPath = "";
        try {
            if (TextUtils.isEmpty(exterPath)) {
                Map<String, String> map = System.getenv();
                Set<String> set = System.getenv().keySet();
                Iterator<String> keys = set.iterator();
                while (keys.hasNext()) {
                    String key = keys.next();
                    String value = map.get(key);
                    if ("SECONDARY_STORAGE".equals(key)) {
                        if (!TextUtils.isEmpty(value) && value.contains(":")) {
                            exterPath = value.split(":")[0];
                        } else {
                            exterPath = value;
                        }
                        return exterPath;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exterPath;
    }

}
