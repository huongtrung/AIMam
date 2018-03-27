package vn.gmorunsystem.aimam.ui.activities;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import vn.gmorunsystem.aimam.R;
import vn.gmorunsystem.aimam.apis.request.LoginWithThirdPartyRequest;
import vn.gmorunsystem.aimam.apis.request.SendDeviceInfoRequest;
import vn.gmorunsystem.aimam.apis.response.LoginResponse;
import vn.gmorunsystem.aimam.apis.response.SendDeviceInfoResponse;
import vn.gmorunsystem.aimam.apis.volley.callback.ApiObjectCallBack;
import vn.gmorunsystem.aimam.constants.APIConstant;
import vn.gmorunsystem.aimam.constants.APPConstant;
import vn.gmorunsystem.aimam.instagramhelper.InstagramApp;
import vn.gmorunsystem.aimam.ui.fragment.LoginFragment;
import vn.gmorunsystem.aimam.utils.AppUtils;
import vn.gmorunsystem.aimam.utils.CheckErrorCodeUtil;
import vn.gmorunsystem.aimam.utils.DebugLog;
import vn.gmorunsystem.aimam.utils.DialogUtil;
import vn.gmorunsystem.aimam.utils.NetworkUtils;
import vn.gmorunsystem.aimam.utils.SharedPrefUtils;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class LoginActivity extends BaseActivity {
    public static final int MULTIPLE_PERMISSON = 1234;
    public static final String LOGIN_FRAG = "LOGIN_FRAG";
    private static final String MALE = "male";
    private static final String FEMALE = "female";
    int sex;
    String birthDay;
    private final List<String> permissions = Arrays.asList("public_profile", "email", "user_friends","user_birthday");
    public static final String manifestPermission[] = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

    CallbackManager fbCallbackManager;
    LoginManager fbLoginManager;

    InstagramApp instagramHelper;
    InstagramApp.OAuthAuthenticationListener listener = new InstagramApp.OAuthAuthenticationListener() {
        @Override
        public void onSuccess() {
            String email = APIConstant.PROVIDER_INSTAGRAM + "_" + SharedPrefUtils.getCurrentInstagramUserId() + "@auth.baris";
            sendRegistByThirdPartyRequest(APIConstant.PROVIDER_INSTAGRAM, SharedPrefUtils.getCurrentInstagramUserId(),
                    email, SharedPrefUtils.getCurrentInstagramUserName(), SharedPrefUtils.getCurrentInstagramUserAvatar(),0,null);
        }

        @Override
        public void onFail(String error) {
            DialogUtil.showDialog(LoginActivity.this, error);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        addFragment(R.id.container, LoginFragment.newInstance(), LOGIN_FRAG);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPMS();
        }

        printKeyHash(this);
        FacebookSdk.sdkInitialize(getApplicationContext());
        fbCallbackManager = CallbackManager.Factory.create();
        fbLoginManager = LoginManager.getInstance();
        instagramHelper = new InstagramApp(this, getString(R.string.instagram_client_id),
                getString(R.string.instagram_client_secret), getString(R.string.instagram_callback_url));
        instagramHelper.setListener(listener);

    }

    @Override
    public void onBackPressed() {
        int backStackCnt = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackCnt > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            System.exit(0);
        }
    }

    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (android.content.pm.Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }
        DebugLog.e("key :" + key);
        return key;
    }

    public void loginByFacebook() {
        fbLoginManager.logInWithReadPermissions(LoginActivity.this, permissions);
        fbLoginManager.registerCallback(fbCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {
                                    String userId = object.getString("id");
                                    String userName = object.getString("name");
                                    String email = APIConstant.PROVIDER_FB + "_" + userId + "@auth.baris";
                                    if (object.has("email") && !object.getString("email").isEmpty()) {
                                        email = object.getString("email");
                                    }
                                    //Get large avatar from userId
                                    String avatarUrl = "https://graph.facebook.com/" + userId + "/picture?type=large";
                                    if (object.has("gender")){
                                        if (object.getString("gender").equals(MALE)){
                                            sex = 1;
                                        } else if (object.getString("gender").equals(FEMALE)){
                                            sex = 0;
                                        }
                                    }
                                    if (object.has("birthday")){
                                        birthDay = object.getString("birthday");
                                    }
                                    sendRegistByThirdPartyRequest(APIConstant.PROVIDER_FB, userId, email, userName, avatarUrl,sex,birthDay);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name, email,friends, picture");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                if (error.getMessage().contains(getString(R.string.login_as_difference_fb_acc_error))) {
                    DialogUtil.showDialogFull(LoginActivity.this, getString(R.string.warning), getString(R.string.login_as_difference_fb_acc_message),
                            getString(R.string.title_yes), getString(R.string.title_no), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    LoginManager.getInstance().logOut();
                                    loginByFacebook();
                                }
                            }, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                } else {
                    Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void loginByInstagram() {
        if (instagramHelper.hasAccessToken()) {
            String email = APIConstant.PROVIDER_INSTAGRAM + "_" + SharedPrefUtils.getCurrentInstagramUserId() + "@auth.baris";
            sendRegistByThirdPartyRequest(APIConstant.PROVIDER_INSTAGRAM, SharedPrefUtils.getCurrentInstagramUserId(), email,
                    SharedPrefUtils.getCurrentInstagramUserName(), SharedPrefUtils.getCurrentInstagramUserAvatar(),0,null);
        } else instagramHelper.authorize();
    }

    public void sendRegistByThirdPartyRequest(final String provider, String uid, String email, String fullName, String avatarUrl,int sex,String birthDay) {
        if (NetworkUtils.getInstance(this).isNetworkConnected()) {
            LoginWithThirdPartyRequest request = new LoginWithThirdPartyRequest(provider, uid, email, fullName, avatarUrl,sex,birthDay);
            request.setRequestCallBack(new ApiObjectCallBack<LoginResponse>() {
                @Override
                public void onSuccess(LoginResponse data) {
                    hideProgressBar();
                    switch (provider) {
                        case APIConstant.PROVIDER_INSTAGRAM:
                            SharedPrefUtils.saveTypeLogin(APPConstant.TYPE_INSTAGRAM);
                            SharedPrefUtils.saveUserInfo(data.userId, data.fullName, data.avatarImageUrl);
                            break;
                        case APIConstant.PROVIDER_FB:
                            SharedPrefUtils.saveTypeLogin(APPConstant.TYPE_FACEBOOK);
                            SharedPrefUtils.saveUserInfo(data.userId, data.fullName, data.avatarImageUrl);
                            break;
                    }
                    if (data != null) {
                        SharedPrefUtils.saveUserInfo(data.userId, data.fullName, data.avatarImageUrl);
                        if (!SharedPrefUtils.getStatusSendDeviceInfo()) {
                            getSendDeviceInfoRequest(data.userId);
                        } else {
                            AppUtils.goToMainOrAdScreen(LoginActivity.this);
                        }
                    } else {
                        DialogUtil.showDialog(LoginActivity.this, getString(R.string.error_server));
                    }
                }

                @Override
                public void onFail(int failCode, String message) {
                    hideProgressBar();
                    CheckErrorCodeUtil.showDialogSpecialError(LoginActivity.this, failCode, message, APPConstant.API_LOGIN_SOCIAL);
                }
            });
            request.execute();
            showProgressBar();
        } else DialogUtil.showDialog(this, getString(R.string.no_connect_internet));
    }

    public void getSendDeviceInfoRequest(int userId) {
        SendDeviceInfoRequest sendDeviceInfoRequest = new SendDeviceInfoRequest();
        sendDeviceInfoRequest.setUserId(userId);
        sendDeviceInfoRequest.setFireBase_key(SharedPrefUtils.getFireBaseToken());
        sendDeviceInfoRequest.setDeviceId(SharedPrefUtils.getDeviceId());
        sendDeviceInfoRequest.setRequestCallBack(new ApiObjectCallBack<SendDeviceInfoResponse>() {
            @Override
            public void onSuccess(SendDeviceInfoResponse data) {
                SharedPrefUtils.saveStatusSendDeviceInfo(data.message);
                AppUtils.goToMainOrAdScreen(LoginActivity.this);
            }

            @Override
            public void onFail(int failCode, String message) {
                AppUtils.goToMainOrAdScreen(LoginActivity.this);
                DebugLog.e("error" + failCode + " " + message);
            }
        });
        sendDeviceInfoRequest.execute();
    }

    private void checkPMS() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    manifestPermission, MULTIPLE_PERMISSON);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fbCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSON: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                            if (ContextCompat.checkSelfPermission(this,
                                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                            }
                        }
                    }
                } else {
                }
                return;
            }

        }
    }



}
