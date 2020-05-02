package com.example.app.utils;

import android.util.Log;

/**
 * <pre>
 *     author : 高勤
  *     e-mail : 984992087@qq.com
  *     time   : 2017/08/07
  *     desc   : 日誌的工具類
  *     version: 1.0
 * </pre>
 */


public class LogUtil {

    private static boolean bossLog = true;

    public static void d(String TAG, String msg, boolean isLog) {
        if (bossLog && isLog)
            Log.d(TAG, msg);
    }

    public static void e(String TAG, String msg, boolean isLog) {
        if (bossLog && isLog)
            Log.e(TAG, msg);
    }

}
