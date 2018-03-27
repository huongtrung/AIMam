package vn.gmorunsystem.aimam.constants;


public class APIConstant {
    public static final int REQUEST_TIMEOUT = 10000;

    //Api test
    public final static String REAL_URL = "http://api.aimam.vn/v1";

    public final static String ERROR = "errors";
    public final static String LOGIN_URL = REAL_URL + "/login";
    public static final String REGISTER_URL = REAL_URL + "/register";
    public static final String COUPON_LIST_URL = REAL_URL + "/coupons?page=";
    public static final String COUPON_DETAIL_URL = REAL_URL + "/coupons/";
    public static final String GET_SHOP_SUBSCRIBE_URL = REAL_URL + "/shops/subscribes/";
    public static final String UN_SUBSCRIBED_URL = REAL_URL + "/subscribes/";
    public static final String SHOP_NEAR_URL = REAL_URL + "/shops/near";
    public static final String PROFILE_URL = REAL_URL + "/users/";
    public static final String INVITE_FRIEND_URL = REAL_URL + "/invite_friend";
    public static final String DETAIL_ITEM_URL = REAL_URL + "/items/";
    public static final String HOME_URL = REAL_URL + "/home";
    public static final String FEEDBACK_URL = REAL_URL + "/feedback";
    public static final String SHOP_HOT_URL = REAL_URL + "/shops/hot/";
    public static final String SHOP_MENU_URL = REAL_URL + "/shops/items?";
    public static final String SHOP_REVIEW_URL = REAL_URL + "/shops/reviews?shop_id=";
    public static final String SEND_SHOP_REVIEW_URL = REAL_URL + "/shops/reviews/";
    public static final String GET_SHOP_URL = REAL_URL + "/shops/";
    public static final String TAKE_COUPON_URL = REAL_URL + "/coupons/";
    public static final String CONTACT_US_URL = REAL_URL + "/contact";
    public static final String SUBSCRIBED_URL = REAL_URL + "/subscribes";
    public static final String GET_MESSAGE_LIST_URL = REAL_URL + "/messages?page=";
    public static final String GET_MESSAGE_DETAIL_URL = REAL_URL + "/messages/";
    public static final String GET_SHOP_STAMP_URL = REAL_URL + "/shops/stamps";
    public static final String SEND_ITEM_REVIEW_URL = REAL_URL + "/items/reviews/";
    public static final String FAVOURTIE_URL = REAL_URL + "/favourite";
    public static final String GET_SHOP_SEARCH_URL = REAL_URL + "/shops/search?";
    public static final String IMG_UPLOAD_URL = REAL_URL + "/images/upload";
    public static final String GET_SHOP_SLIDER_URL = REAL_URL + "/shops/slideshows?";
    public static final String UPDATE_PROFILE_URL = REAL_URL + "/users/";
    public static final String DELETE_MESSAGE_URL = REAL_URL + "/messages/";
    public static final String USEFUL_URL = REAL_URL + "/reviews/useful/";
    public static final String CHECK_IN_URL = REAL_URL + "/shops/checkins";
    public static final String SHOP_NEAREST = REAL_URL + "/shops/nearest?";
    public static final String NEW_FEED_URL = REAL_URL + "/new_feeds?page=";
    public static final String SEND_DEVICE_INFO_URL = REAL_URL + "/set_firebase";
    public static final String REGISTER_BY_THIRD_PARTY = REAL_URL + "/third_party_register";
    public static final String FACEBOOK_URL = "https://fb.me/493857950766025";
    public static final String WEB_URL = "http://aimam.vn/";

    //param
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String FULL_NAME = "full_name";
    public static final String CONTENT = "content";
    public static final String SHOP_ID = "shop_id";
    public static final String TAKE_COUPON = "/take";
    public static final String TOP_LEFT_LAT = "top_left_latitude";
    public static final String TOP_LEFT_LONG = "top_left_longtitude";
    public static final String BOT_RIGHT_LAT = "bot_right_latitude";
    public static final String BOT_RIGHT_LONG = "bot_right_longtitude";
    public static final String USER_NAME = "user_name";
    public static final String USER_ID = "user_id";
    public static final String USER_PHONE = "phone";
    public static final String TITLE = "title";
    public static final String ITEM_ID = "item_id";
    public static final String VOTE = "vote";
    public static final String REVIEW_ID = "review_id";
    public static final String KEYWORD = "keyword";
    public static final String IMAGE = "image_";
    public static final String INFO = "infor_";
    public static final String AVATAR_IMG = "avatar_image_url";
    public static final String SEX = "sex";
    public static final String JOB = "job";
    public static final String ADDRESS = "address";
    public static final String WEB = "website";
    public static final String DESCRIPTION = "description";
    public static final String BIRTHDAY = "birthday";
    public static final String HASHTAG = "hashtags";
    public static final String COOKIE = "Cookie";
    public static final String PROVIDER = "provider";
    public static final String PROVIDER_FB = "facebook";
    public static final String PROVIDER_INSTAGRAM = "instagram";
    public static final String AVATAR = "avatar";
    public static final String LATITUDE = "lat";
    public static final String LONGITUDE = "lon";
    public static final String LONGITUDE_STAMP = "long";
    public static final String LIMIT_SHOP = "limit_shop";
    public static final String STAMP_POSITION = "position";
    public static final String DEVICE_TYPE = "device_type";
    public static final String FIREBASE_KEY = "firebase_key";
    public static final String DEVICE_UID = "device_uid";
    public static final String TYPE_ANDROID = "android";
    public static final String TYPE_ERROR = "type";
    public static final String ERROR_VALUE = "errors";

    //Error code
    public static final int ERROR_CODE_999 = 999;
    public static final int ERROR_CODE_998_JSON_SYNTAX = 998;
    public static final int ERROR_CODE_500 = 500;
    public static final int ERROR_CODE_404 = 404;
    public static final int ERROR_CODE_401 = 401;
    public static final int ERROR_CODE_400 = 400;
    public static final int ERROR_CODE_409 = 409;
}
