package vn.gmorunsystem.aimam.constants;


import android.os.Environment;

import java.io.File;

public class APPConstant {
    public static final String ACCESS_TOKEN = "access-token";
    public static final String CLIENT = "client";
    public static final String UID = "uid";
    public static final String USER_ID = "user_id";
    public static final String USER_NAME = "user_name";
    public static final String JUST_INSTALL_APP = "1st time run app";

    public static final String FOLDER_NAME = "AiMăm";
    public static final String DIRECTORY = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separatorChar
            + Environment.DIRECTORY_PICTURES + File.separatorChar + FOLDER_NAME;
    public static final String DIRECTORY_DOWNLOAD = DIRECTORY + File.separatorChar + "Download";
    public static final String AVATAR_URL = "avatar url";
    public static final double NOT_STORE_LOCATION_OR_USER_LOCATION = -1;
    public static final int SIZE_LIST_IS_ONE = 1;
    public static final int SIZE_LIST_IS_TWO = 2;
    public static final int SIZE_LIST_IS_THREE = 3;
    public static final int RESULT_CODE = 0;
    public static final int REQUEST_CODE = 1;
    public static final int FRAG_ON = 1;
    public static final int FRAG_OFF = 0;
    public static final String FRAG_SWITCH_STATUS = "FRAG_SWITCH_STATUS";
    public static final String NOTIFICATION = "PUT_NOTIFICATION";
    public static final int FRAG_MAIN = 1;
    public static final int FRAG_SEARCH = 0;
    public static final int FRAG_ZERO = 0;
    public static final String FIREBASE_TOKEN = "FIREBASE_TOKEN";
    public static final String DEVICE_ID = "DEVICE_ID";
    public static final String STATUS_SEND = "STATUS_SEND";
    public static final String CURRENT_DATE = "CURRENT_DATE";

    //Instagram params
    public static final String INSTAGRAM_API_USERNAME = "instagram_username";
    public static final String INSTAGRAM_API_USER_ID = "instagram_id";
    public static final String INSTAGRAM_API_USER_AVATAR = "instagram_avatar";
    public static final String INSTAGRAM_API_ACCESS_TOKEN = "instagram_access_token";
    public static final String CLIENT_ID = "4525ea4184ae4bea9801ffc44ee55798";
    public static final String CLIENT_SECRET = "5c49b5080bda43f3864d5c49251ce9c6";
    public static final String CALLBACK_URL = "http://google.com";
    public static final String INSTAGRAM_PACKAGE = "com.instagram.android";
    public static final String TYPE_LOGIN = "TYPE_LOGIN";
    public static final String TYPE_LANGUAGE = "TYPE_LANGUAGE";
    public static final int TYPE_LOCAL_ACCOUNT = 0;
    public static final int TYPE_FACEBOOK = 1;
    public static final int TYPE_INSTAGRAM = 2;
    public static final String TYPE_RADIUS= "checkin_radius";
    public static final String TYPE_NUMBER_LIMIT= "stamp_number_limit";
    public static final String TYPE_TIME= "time_between_checkins";
    public static final String TYPE_TAKEN= "taken";
    public static final String TYPE_NOT_LIVE= "not_live";
    public static final String ERROR_EMAIL= "email";

    //TAG
    public static final String COUPON_LIST_FRAG_TAG = "COUPON_LIST_FRAG_TAG";
    public static final String COUPON_DETAIL_FRAG_TAG = "COUPON_DETAIL_FRAG_TAG";
    public static final String PROFILE_FRAG_TAG = "PROFILE_FRAG_TAG";
    public static final String UPDATE_PROFILE_FRAG_TAG = "UPDATE_PROFILE_FRAG_TAG";
    public static final String MESSAGE_LIST_FRAG_TAG = "MESSAGE_LIST_FRAG_TAG";
    public static final String SETTING_FRAG_TAG = "SETTING_FRAG_TAG";
    public static final String INVITE_FRAG_TAG = "INVITE_FRAG_TAG";
    public static final String ABOUT_FRAG_TAG = "ABOUT_FRAG_TAG";
    public static final String MAIN_FRAG_TAG = "MAIN_FRAG_TAG";
    public static final String SHOP_FAVORITE_FRAG_TAG = "SHOP_FAVORITE_FRAG_TAG";
    public static final String NEAR_BY_FRAG_TAG = "NEAR_BY_FRAG_TAG";
    public static final String PHOTO_MANAGER_BY_FRAG_TAG = "PHOTO_MANAGER_BY_FRAG_TAG";
    public static final int TYPE_SHOP = 1;
    public static final int TYPE_ITEM = 2;
    public static final int TYPE_COUPON = 3;
    public static final String API_CHECK_IN = "API_CHECK_IN";
    public static final String API_GET_GIFT = "API_GET_STAMP";
    public static final String API_REGISTER = "API_REGISTER";
    public static final String API_LOGIN = "API_LOGIN";
    public static final String API_LOGIN_SOCIAL = "API_LOGIN_SOCIAL";
}
