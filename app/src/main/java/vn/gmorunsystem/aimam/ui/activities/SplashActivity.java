package vn.gmorunsystem.aimam.ui.activities;

import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings.Secure;
import android.support.v4.content.ContextCompat;
import android.widget.ProgressBar;

import butterknife.BindView;
import vn.gmorunsystem.aimam.R;
import vn.gmorunsystem.aimam.apis.request.LoginWithAccessToken;
import vn.gmorunsystem.aimam.apis.request.SendDeviceInfoRequest;
import vn.gmorunsystem.aimam.apis.response.LoginResponse;
import vn.gmorunsystem.aimam.apis.response.SendDeviceInfoResponse;
import vn.gmorunsystem.aimam.apis.volley.callback.ApiObjectCallBack;
import vn.gmorunsystem.aimam.constants.APPConstant;
import vn.gmorunsystem.aimam.utils.AppUtils;
import vn.gmorunsystem.aimam.utils.CheckErrorCodeUtil;
import vn.gmorunsystem.aimam.utils.DebugLog;
import vn.gmorunsystem.aimam.utils.IntentUtil;
import vn.gmorunsystem.aimam.utils.NetworkUtils;
import vn.gmorunsystem.aimam.utils.SharedPrefUtils;
import vn.gmorunsystem.aimam.utils.ToastUtil;
import vn.gmorunsystem.aimam.utils.UiUtil;

public class SplashActivity extends BaseActivity {
    private static long SLEEP_TIME = 2000;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    Handler handler;
    private String androidId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getAndroidDeviceId();
        handler = new Handler();
        progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.color_green), android.graphics.PorterDuff.Mode.MULTIPLY);

        if (SharedPrefUtils.getBoolean(APPConstant.JUST_INSTALL_APP, true)) {
            gotoTutorial();
            SharedPrefUtils.saveStatusNotify(true);
        } else {
            if (SharedPrefUtils.checkCurrentAccess()) {
                loginWithUserAccount();
            } else {
                loginWithAccessToken();
            }
        }
    }

    private void loginWithAccessToken() {
        if (NetworkUtils.getInstance(this).isNetworkConnected()) {
            UiUtil.showView(progressBar);
            LoginWithAccessToken loginWithAccessToken = new LoginWithAccessToken();
            loginWithAccessToken.setRequestCallBack(new ApiObjectCallBack<LoginResponse>() {
                @Override
                public void onSuccess(LoginResponse data) {
                    UiUtil.hideView(progressBar, true);
                    SharedPrefUtils.saveTypeLogin(APPConstant.TYPE_LOCAL_ACCOUNT);
                    SharedPrefUtils.saveUserInfo(data.userId, data.fullName, data.avatarImageUrl);

                    if (!SharedPrefUtils.getStatusSendDeviceInfo()) {
                        getSendDeviceInfoRequest(data.userId);
                    } else {
                        AppUtils.goToMainOrAdScreen(SplashActivity.this);
                    }
                }

                @Override
                public void onFail(int failCode, String message) {
                    UiUtil.hideView(progressBar, true);
                    CheckErrorCodeUtil.showDialogError(SplashActivity.this, failCode);
                    IntentUtil.gotoLogin(SplashActivity.this);
                }
            });
            loginWithAccessToken.execute();
        } else {
            ToastUtil.makeText(SplashActivity.this, getString(R.string.no_connect_internet));
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, SLEEP_TIME);
        }
    }

    public void getSendDeviceInfoRequest(int userId) {
        SendDeviceInfoRequest sendDeviceInfoRequest = new SendDeviceInfoRequest();
        sendDeviceInfoRequest.setUserId(userId);
        sendDeviceInfoRequest.setFireBase_key(SharedPrefUtils.getFireBaseToken());
        sendDeviceInfoRequest.setDeviceId(androidId);
        sendDeviceInfoRequest.setRequestCallBack(new ApiObjectCallBack<SendDeviceInfoResponse>() {
            @Override
            public void onSuccess(SendDeviceInfoResponse data) {
                SharedPrefUtils.saveStatusSendDeviceInfo(data.message);
                AppUtils.goToMainOrAdScreen(SplashActivity.this);
            }

            @Override
            public void onFail(int failCode, String message) {
                AppUtils.goToMainOrAdScreen(SplashActivity.this);
                DebugLog.e("error" + failCode + " " + message);
            }
        });
        sendDeviceInfoRequest.execute();
    }

    private void getAndroidDeviceId() {
        androidId = Secure.getString(this.getContentResolver(), Secure.ANDROID_ID);
        SharedPrefUtils.saveDeviceId(androidId);
        DebugLog.e("Android ID : " + androidId);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.exit(0);
    }

    private void loginWithUserAccount() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                IntentUtil.gotoLogin(SplashActivity.this);
            }
        }, SLEEP_TIME);
    }

    private void gotoTutorial() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                IntentUtil.gotoTutorial(SplashActivity.this);
            }
        }, SLEEP_TIME);
    }


}
