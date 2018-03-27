package vn.gmorunsystem.aimam.utils;


import android.content.Context;

import java.util.Map;

import vn.gmorunsystem.aimam.App;
import vn.gmorunsystem.aimam.constants.APPConstant;

public class SharedPrefUtils {


    // // internal handle
    private SecurePreferences preferences;

    /**
     * @param context your current context.
     */
    public SharedPrefUtils(Context context) {
        this.preferences = new SecurePreferences(context);
    }

    public static void saveStatusSendDeviceInfo(boolean status) {
        SharedPrefUtils.putBoolean(APPConstant.STATUS_SEND, status);
    }

    public static boolean getStatusSendDeviceInfo() {
        return SharedPrefUtils.getBoolean(APPConstant.STATUS_SEND, false);
    }

    public static void saveDeviceId(String id) {
        if (StringUtil.isEmpty(SharedPrefUtils.getDeviceId()))
            SharedPrefUtils.putString(APPConstant.DEVICE_ID, id);
    }

    public static String getDeviceId() {
        return SharedPrefUtils.getString(APPConstant.DEVICE_ID, "");
    }

    public static void saveFireBaseToken(String token) {
        SharedPrefUtils.putString(APPConstant.FIREBASE_TOKEN, token);
    }

    public static String getFireBaseToken() {
        return SharedPrefUtils.getString(APPConstant.FIREBASE_TOKEN, "");
    }

    public static void saveLanguageSetting(int languageCode) {
        SharedPrefUtils.putInt(APPConstant.TYPE_LANGUAGE, languageCode);
    }

    public static int getLanguageSetting() {
        return SharedPrefUtils.getInt(APPConstant.TYPE_LANGUAGE, LanguageUtils.LANG_VI);
    }

    public static void saveTypeLogin(int type) {
        SharedPrefUtils.putInt(APPConstant.TYPE_LOGIN, type);
    }

    public static int getTypeLogin() {
        return SharedPrefUtils.getInt(APPConstant.TYPE_LOGIN, -1);
    }

    private static void saveAccessToken(String token) {
        SharedPrefUtils.putString(APPConstant.ACCESS_TOKEN, token);
    }

    public static void saveAvatarUrl(String url) {
        SharedPrefUtils.putString(APPConstant.AVATAR_URL, url);
    }

    private static void saveClient(String client) {
        SharedPrefUtils.putString(APPConstant.CLIENT, client);
    }

    private static void saveUID(String uid) {
        SharedPrefUtils.putString(APPConstant.UID, uid);
    }

    public static String getUserName() {
        return SharedPrefUtils.getString(APPConstant.USER_NAME, "");
    }

    public static void saveUserName(String userName) {
        SharedPrefUtils.putString(APPConstant.USER_NAME, userName);
    }

    public static Integer getUserId() {
        return SharedPrefUtils.getInt(APPConstant.USER_ID, -1);
    }

    public static void saveUserInfo(int userId, String userName, String url) {
        SharedPrefUtils.putInt(APPConstant.USER_ID, userId);
        SharedPrefUtils.putString(APPConstant.USER_NAME, userName);
        SharedPrefUtils.putString(APPConstant.AVATAR_URL, url);
    }

    public static void removeUserInfo() {
        SharedPrefUtils.removeKey(APPConstant.USER_ID);
        SharedPrefUtils.removeKey(APPConstant.USER_NAME);
        SharedPrefUtils.removeKey(APPConstant.AVATAR_URL);
    }

    public static String getAccessToken() {
        return SharedPrefUtils.getString(APPConstant.ACCESS_TOKEN, "");
    }

    public static void saveStatusNotify(boolean status) {
        if (status) {
            SharedPrefUtils.putInt(APPConstant.FRAG_SWITCH_STATUS, APPConstant.FRAG_ON);
        } else {
            SharedPrefUtils.putInt(APPConstant.FRAG_SWITCH_STATUS, APPConstant.FRAG_OFF);
        }
    }

    public static int getStatusNotify() {
        return SharedPrefUtils.getInt(APPConstant.FRAG_SWITCH_STATUS, 1);
    }

    public static String getClient() {
        return SharedPrefUtils.getString(APPConstant.CLIENT, "");
    }

    public static String getUID() {
        return SharedPrefUtils.getString(APPConstant.UID, "");
    }

    public static String getAvatarUrl() {
        return SharedPrefUtils.getString(APPConstant.AVATAR_URL, "");
    }

    public static boolean checkCurrentAccess() {
        if (!StringUtil.isEmpty(getAccessToken()))
            return false;
        else if (!StringUtil.isEmpty(getClient()))
            return false;
        else if (!StringUtil.isEmpty(getUID()))
            return false;
        return true;
    }

    //Start block instagram
    public static void saveInstagramUserInfo(String accessToken, String userId, String userName, String avatarUrl) {
        SharedPrefUtils.putString(APPConstant.INSTAGRAM_API_ACCESS_TOKEN, accessToken);
        SharedPrefUtils.putString(APPConstant.INSTAGRAM_API_USER_ID, userId);
        SharedPrefUtils.putString(APPConstant.INSTAGRAM_API_USERNAME, userName);
        SharedPrefUtils.putString(APPConstant.INSTAGRAM_API_USER_AVATAR, avatarUrl);
    }

    public static void removeCurrentInstagramUserInfo() {
        SharedPrefUtils.removeKey(APPConstant.INSTAGRAM_API_ACCESS_TOKEN);
        SharedPrefUtils.removeKey(APPConstant.INSTAGRAM_API_USER_ID);
        SharedPrefUtils.removeKey(APPConstant.INSTAGRAM_API_USERNAME);
        SharedPrefUtils.removeKey(APPConstant.INSTAGRAM_API_USER_AVATAR);
    }

    public static String getCurrentInstagramUserName() {
        return SharedPrefUtils.getString(APPConstant.INSTAGRAM_API_USERNAME, "");
    }

    public static String getCurrentInstagramUserId() {
        return SharedPrefUtils.getString(APPConstant.INSTAGRAM_API_USER_ID, "");
    }

    public static String getCurrentInstagramUserAvatar() {
        return SharedPrefUtils.getString(APPConstant.INSTAGRAM_API_USER_AVATAR, "");
    }

    public static String getCurrentInstagramUserAccessToken() {
        return SharedPrefUtils.getString(APPConstant.INSTAGRAM_API_ACCESS_TOKEN, "");
    }
    //End block instagram

    public static void saveCurrentAccess(Map<String, String> headers) {
        SharedPrefUtils.saveAccessToken(headers.get(APPConstant.ACCESS_TOKEN));
        SharedPrefUtils.saveClient(headers.get(APPConstant.CLIENT));
        SharedPrefUtils.saveUID(headers.get(APPConstant.UID));
    }

    public static void removeCurrentAccess() {
        SharedPrefUtils.removeKey(APPConstant.ACCESS_TOKEN);
        SharedPrefUtils.removeKey(APPConstant.CLIENT);
        SharedPrefUtils.removeKey(APPConstant.UID);
    }

    public static void saveCurrentDate(String currentDate){
        SharedPrefUtils.putString(APPConstant.CURRENT_DATE,currentDate);
    }

    public static String getSavedCurrentDate(){
        return SharedPrefUtils.getString(APPConstant.CURRENT_DATE,"");
    }

    private void putStringOrReplace(String key, String value) {
        if (value == null) {
            preferences.edit().remove(key).commit();
        } else {
            preferences.edit().putString(key, value).commit();
        }
    }

    private String getStringFromSharedPre(String key, String defValue) {
        if (preferences.contains(key)) {
            return preferences.getString(key, defValue);
        }
        return defValue;
    }

    private void putBooleanToShare(String key, boolean value) {
        preferences.edit().putBoolean(key, value).commit();
    }

    private Boolean getBooleanFromShare(String key, boolean defValue) {
        if (preferences.contains(key)) {
            return preferences.getBoolean(key, defValue);
        } else {
            return defValue;
        }
    }

    private void putIntToShare(String key, int value) {
        preferences.edit().putInt(key, value).commit();
    }

    private int getIntFromShare(String key, int defValue) {
        if (preferences.contains(key)) {
            return preferences.getInt(key, defValue);
        } else {
            return defValue;
        }
    }

    private void putLongToShare(String key, long value) {
        preferences.edit().putLong(key, value).commit();
    }

    private long getLongFromShare(String key, long defValue) {
        if (preferences.contains(key)) {
            return preferences.getLong(key, defValue);
        } else {
            return defValue;
        }
    }

    private void putFloatToShare(String key, float value) {
        preferences.edit().putFloat(key, value).commit();
    }

    private float getFloatFromShare(String key, float defValue) {
        if (preferences.contains(key)) {
            return preferences.getFloat(key, defValue);
        } else {
            return defValue;
        }
    }

    private boolean containKEY(String key) {
        return preferences.contains(key);
    }

    private void deleteKey(String key) {
        if (preferences.contains(key)) {
            preferences.edit().remove(key).commit();
        }
    }

    public static void putString(String key, String value) {
        App.getSharedPreferences().putStringOrReplace(key, value);
    }

    public static String getString(String key, String defValue) {
        return App.getSharedPreferences().getStringFromSharedPre(key, defValue);
    }

    public static void putInt(String key, int value) {
        App.getSharedPreferences().putIntToShare(key, value);
    }

    public static int getInt(String key, int defValue) {
        return App.getSharedPreferences().getIntFromShare(key, defValue);
    }

    public static void putLong(String key, int value) {
        App.getSharedPreferences().putLongToShare(key, value);
    }

    public static long getLong(String key, int defValue) {
        return App.getSharedPreferences().getLongFromShare(key, defValue);
    }

    public static void putBoolean(String key, boolean value) {
        App.getSharedPreferences().putBooleanToShare(key, value);
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return App.getSharedPreferences().getBooleanFromShare(key, defValue);
    }

    public static void putFloat(String key, float value) {
        App.getSharedPreferences().putFloatToShare(key, value);
    }

    public static float getFloat(String key, float defValue) {
        return App.getSharedPreferences().getFloatFromShare(key, defValue);
    }

    public static boolean containKey(String key) {
        return App.getSharedPreferences().containKEY(key);
    }

    public static void removeKey(String key) {
        App.getSharedPreferences().deleteKey(key);
    }

}
