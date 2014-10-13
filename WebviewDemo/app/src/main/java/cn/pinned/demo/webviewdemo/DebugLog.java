package cn.pinned.demo.webviewdemo;

import android.util.Log;

/**
 * Created by luozc on Aug 11, 2014
 */

public class DebugLog {
    private static boolean DEBUG = false;

    public static void setDebugMode(boolean debugMode){
        DEBUG = debugMode;
    }

    public static void v(String tag, String msg) {
        if (DEBUG) {
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (DEBUG) {
            if (msg == null) {
                msg = "null";
            }
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (DEBUG) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (DEBUG) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (DEBUG) {
            Log.e(tag, msg);
        }
    }

}
