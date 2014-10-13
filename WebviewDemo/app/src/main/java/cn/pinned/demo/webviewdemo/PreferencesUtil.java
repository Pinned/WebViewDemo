package cn.pinned.demo.webviewdemo;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by luozc on Aug 7, 2014
 */
public final class PreferencesUtil {
	private static final String TAG = PreferencesUtil.class.getSimpleName();

	
	private static SharedPreferences getPreferences(Context context) {
		return context.getSharedPreferences("webview_demo", Context.MODE_PRIVATE);
	}
    public static boolean remove(Context context, String key) {
        if (key != null) {
            SharedPreferences preferences = getPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            return editor.remove(key).commit();
        }
        return false;
    }


    /**
     * 从preferences中取值，如果不存在，则返回null
     * @param context
     * @param key
     * @return
     */
    public static String getString(Context context, String key) {
        return getString(context, key, null);
    }
    /**
     * 从preferences中取值，如果不存在，则返回defaultVal
     * @param context
     * @param key
     * @param value
     * @return
     */
    public static String getString(Context context, String key,String defaultVal) {
    	SharedPreferences preferences = getPreferences(context);
    	return preferences.getString(key, defaultVal);
    }


    public static boolean setString(Context context, String key, String value) {
    	DebugLog.d(TAG, "[setString] key:" + key + ",value:" + value);
        if (value != null) {
            SharedPreferences preferences = getPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            return editor.putString(key, value).commit();
        }
        return false;
    }

    /**
     * 从preferences中取值，如果不存在，则返回false
     * @param context
     * @param key
     * @return
     */
    public static boolean getBoolean(Context context, String key) {
        return getBoolean(context, key, false);
    }

    /**
     * 从preferences中取值，如果不存在，则返回defaultVal
     * @param context
     * @param key
     * @param defaultVal
     * @return
     */
    public static boolean getBoolean(Context context,String key,boolean defaultVal){
    	SharedPreferences preferences = getPreferences(context);
        return preferences.getBoolean(key, defaultVal);
    }
    
    public static boolean setBoolean(Context context, String key, boolean value) {
        SharedPreferences preferences = getPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        return editor.putBoolean(key, value).commit();
    }

    /**
     * 从Preferences中获取一个整形值，如果没有取到值则返回0
     * @param context
     * @param key
     * @return
     */
    public static int getInt(Context context, String key) {
        return getInt(context, key, 0);
    }
    /**
     * 从Preferences中获取一个整形值，如果没有取到值则返回defaultValue
     * @param context
     * @param key
     * @param defaultValue
     * @return
     */
    public static int getInt(Context context, String key,int defaultValue) {
    	SharedPreferences preferences = getPreferences(context);
    	return preferences.getInt(key, defaultValue);
    }

    public static boolean setInt(Context context, String key, int value) {
        SharedPreferences preferences = getPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        return editor.putInt(key, value).commit();
    }

    public static boolean clear(Context context) {
        SharedPreferences preferences = getPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        return editor.clear().commit();
    }

    private PreferencesUtil() {}
}
