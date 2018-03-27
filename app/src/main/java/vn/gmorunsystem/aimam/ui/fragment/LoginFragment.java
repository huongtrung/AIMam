package vn.gmorunsystem.aimam.ui.fragment;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import vn.gmorunsystem.aimam.R;
import vn.gmorunsystem.aimam.apis.request.LoginRequest;
import vn.gmorunsystem.aimam.apis.response.LoginResponse;
import vn.gmorunsystem.aimam.apis.volley.callback.ApiObjectCallBack;
import vn.gmorunsystem.aimam.constants.APPConstant;
import vn.gmorunsystem.aimam.ui.activities.LoginActivity;
import vn.gmorunsystem.aimam.utils.AppUtils;
import vn.gmorunsystem.aimam.utils.CheckErrorCodeUtil;
import vn.gmorunsystem.aimam.utils.NetworkUtils;
import vn.gmorunsystem.aimam.utils.SharedPrefUtils;
import vn.gmorunsystem.aimam.utils.StringUtil;
import vn.gmorunsystem.aimam.utils.ToastUtil;
import vn.gmorunsystem.aimam.utils.UiUtil;

import static vn.gmorunsystem.aimam.utils.DialogUtil.showDialog;

public class LoginFragment extends BaseFragment {
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.tv_error_email)
    TextView tvErrorEmail;
    @BindView(R.id.tv_error_pass)
    TextView tvErrorPass;
    @BindView(R.id.bt_login)
    Button btLogin;

    private String mUserEmail;
    private String mPassword;

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    protected void initView(View root) {
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void getArgument(Bundle bundle) {
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @OnClick({R.id.bt_login, R.id.tv_signUp, R.id.iv_login_fb, R.id.iv_login_instagram})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login:
                loginApp();
                break;
            case R.id.tv_signUp:
                replaceFragment(R.id.container, RegisterFragment.newInstance(), true);
                break;
            case R.id.iv_login_fb:
                ((LoginActivity) getActivity()).loginByFacebook();
                break;
            case R.id.iv_login_instagram:
                ((LoginActivity) getActivity()).loginByInstagram();
                break;
        }
    }

    @OnEditorAction(R.id.et_password)
    public boolean signIn(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            loginApp();
            return true;
        }
        return false;
    }

    private void loginApp() {
        UiUtil.hideView(tvErrorEmail, true);
        UiUtil.hideView(tvErrorPass, true);
        mUserEmail = etEmail.getText().toString().trim();
        mPassword = etPassword.getText().toString().trim();
        if (NetworkUtils.getInstance(getContext()).isNetworkConnected()) {
            if (validateInput()) {
                showProgressBar();
                hideSoftKeyboard();
                LoginRequest loginRequest = new LoginRequest(mUserEmail, mPassword);
                loginRequest.setRequestCallBack(new ApiObjectCallBack<LoginResponse>() {
                    @Override
                    public void onSuccess(LoginResponse data) {
                        hideProgressBar();
                        if (data != null) {
                            SharedPrefUtils.saveTypeLogin(APPConstant.TYPE_LOCAL_ACCOUNT);
                            SharedPrefUtils.saveUserInfo(data.userId, data.fullName, data.avatarImageUrl);
                            if (!SharedPrefUtils.getStatusSendDeviceInfo()) {
                                ((LoginActivity) getActivity()).getSendDeviceInfoRequest(data.userId);
                            } else {
                                AppUtils.goToMainOrAdScreen(getActivity());
                            }
                        } else
                            showDialog(getContext(), getString(R.string.error_server));
                    }

                    @Override
                    public void onFail(int failCode, String message) {
                        hideProgressBar();
                        CheckErrorCodeUtil.showDialogSpecialError(getContext(), failCode, message, APPConstant.API_LOGIN);
                    }
                });
                loginRequest.execute();
            }
        } else {
            ToastUtil.makeText(getContext(), getString(R.string.no_connect_internet));
        }
    }

    private boolean validateInput() {
        if (!StringUtil.checkStringValid(mUserEmail) || !StringUtil.isValidEmail(mUserEmail)) {
            etEmail.setBackgroundResource(R.drawable.bg_error_edit_text);
            UiUtil.showView(tvErrorEmail);
            return false;
        } else if (!StringUtil.checkStringValid(mPassword) || mPassword.length() < 8) {
            etPassword.setBackgroundResource(R.drawable.bg_error_edit_text);
            UiUtil.showView(tvErrorPass);
            return false;
        } else {
            etEmail.setBackgroundResource(R.color.color_gray_line);
            etPassword.setBackgroundResource(R.color.color_gray_line);
            UiUtil.hideView(tvErrorEmail, true);
            UiUtil.hideView(tvErrorPass, true);
            return true;
        }
    }


}
