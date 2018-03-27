package vn.gmorunsystem.aimam.ui.fragment;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import vn.gmorunsystem.aimam.R;
import vn.gmorunsystem.aimam.apis.request.LoginRequest;
import vn.gmorunsystem.aimam.apis.request.RegisterRequest;
import vn.gmorunsystem.aimam.apis.response.LoginResponse;
import vn.gmorunsystem.aimam.apis.response.RegisterResponse;
import vn.gmorunsystem.aimam.apis.volley.callback.ApiObjectCallBack;
import vn.gmorunsystem.aimam.constants.APPConstant;
import vn.gmorunsystem.aimam.ui.activities.LoginActivity;
import vn.gmorunsystem.aimam.utils.AppUtils;
import vn.gmorunsystem.aimam.utils.CheckErrorCodeUtil;
import vn.gmorunsystem.aimam.utils.DialogUtil;
import vn.gmorunsystem.aimam.utils.NetworkUtils;
import vn.gmorunsystem.aimam.utils.SharedPrefUtils;
import vn.gmorunsystem.aimam.utils.StringUtil;
import vn.gmorunsystem.aimam.utils.ToastUtil;
import vn.gmorunsystem.aimam.utils.UiUtil;

public class RegisterFragment extends BaseFragment {
    @BindView(R.id.et_regist_userName)
    EditText edtUsername;
    @BindView(R.id.et_regist_email)
    EditText edtEmail;
    @BindView(R.id.et_regist_password)
    EditText edtPass;
    @BindView(R.id.et_regist_confirmPass)
    EditText edtConfirmPass;
    @BindView(R.id.tv_error_name)
    TextView tvErrorName;
    @BindView(R.id.tv_error_email)
    TextView tvErrorEmail;
    @BindView(R.id.tv_error_password)
    TextView tvErrorPass;
    @BindView(R.id.tv_error_confirm)
    TextView tvErrorConfirmPass;

    private String mUserName;
    private String mEmailUser;
    private String mPassUser;
    private String mConfirmPass;
    private String errorEmail;

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_register;
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

    @OnEditorAction(R.id.et_regist_confirmPass)
    public boolean signIn(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            registerAccount();
            return true;
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @OnClick({R.id.bt_regist, R.id.iv_regist_fb, R.id.iv_regist_instagram})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.bt_regist:
                registerAccount();
                break;
            case R.id.iv_regist_fb:
                if (NetworkUtils.getInstance(getContext()).isNetworkConnected()) {
                    ((LoginActivity) getActivity()).loginByFacebook();
                } else ToastUtil.makeText(getContext(), getString(R.string.no_connect_internet));
                break;
            case R.id.iv_regist_instagram:
                if (NetworkUtils.getInstance(getContext()).isNetworkConnected()) {
                    ((LoginActivity) getActivity()).loginByInstagram();
                } else ToastUtil.makeText(getContext(), getString(R.string.no_connect_internet));
                break;
        }
    }

    public boolean checkFillEdt() {
        if (!StringUtil.checkStringValid(mUserName) || mUserName.length() < 4 || mUserName.length() > 100) {
            edtEmail.setBackgroundResource(R.drawable.et_user_signup);
            edtConfirmPass.setBackgroundResource(R.drawable.et_user_signup);
            edtPass.setBackgroundResource(R.drawable.et_user_signup);
            UiUtil.hideView(tvErrorEmail, true);
            UiUtil.hideView(tvErrorPass, true);
            UiUtil.hideView(tvErrorConfirmPass, true);

            UiUtil.showView(tvErrorName);
            edtUsername.setBackgroundResource(R.drawable.et_user_signup_error);
            return false;
        } else if (!StringUtil.checkStringValid(mEmailUser) || !StringUtil.isValidEmail(mEmailUser)) {
            edtUsername.setBackgroundResource(R.drawable.et_user_signup);
            edtConfirmPass.setBackgroundResource(R.drawable.et_user_signup);
            edtPass.setBackgroundResource(R.drawable.et_user_signup);
            UiUtil.hideView(tvErrorName, true);
            UiUtil.hideView(tvErrorPass, true);
            UiUtil.hideView(tvErrorConfirmPass, true);

            UiUtil.showView(tvErrorEmail);
            edtEmail.setBackgroundResource(R.drawable.et_user_signup_error);
            return false;
        } else if (!StringUtil.checkStringValid(mPassUser) || mPassUser.length() < 8 || mPassUser.length() > 32) {
            edtUsername.setBackgroundResource(R.drawable.et_user_signup);
            edtEmail.setBackgroundResource(R.drawable.et_user_signup);
            edtConfirmPass.setBackgroundResource(R.drawable.et_user_signup);
            UiUtil.hideView(tvErrorName, true);
            UiUtil.hideView(tvErrorEmail, true);
            UiUtil.hideView(tvErrorConfirmPass, true);

            UiUtil.showView(tvErrorPass);
            edtPass.setBackgroundResource(R.drawable.et_user_signup_error);
            return false;
        } else if (!mConfirmPass.equalsIgnoreCase(mPassUser)) {
            edtUsername.setBackgroundResource(R.drawable.et_user_signup);
            edtEmail.setBackgroundResource(R.drawable.et_user_signup);
            edtPass.setBackgroundResource(R.drawable.et_user_signup);
            UiUtil.hideView(tvErrorName, true);
            UiUtil.hideView(tvErrorEmail, true);
            UiUtil.hideView(tvErrorPass, true);

            UiUtil.showView(tvErrorConfirmPass);
            edtConfirmPass.setBackgroundResource(R.drawable.et_user_signup_error);
            edtPass.setBackgroundResource(R.drawable.et_user_signup_error);
            return false;
        } else {
            edtUsername.setBackgroundResource(R.drawable.et_user_signup);
            edtEmail.setBackgroundResource(R.drawable.et_user_signup);
            edtConfirmPass.setBackgroundResource(R.drawable.et_user_signup);
            edtPass.setBackgroundResource(R.drawable.et_user_signup);
            UiUtil.hideView(tvErrorName, true);
            UiUtil.hideView(tvErrorEmail, true);
            UiUtil.hideView(tvErrorPass, true);
            UiUtil.hideView(tvErrorConfirmPass, true);
            return true;
        }
    }

    public void registerAccount() {
        mUserName = edtUsername.getText().toString().trim();
        mEmailUser = edtEmail.getText().toString().trim();
        mPassUser = edtPass.getText().toString().trim();
        mConfirmPass = edtConfirmPass.getText().toString().trim();
        if (NetworkUtils.getInstance(getContext()).isNetworkConnected()) {
            if (checkFillEdt()) {
                showProgressBar();
                RegisterRequest registerRequest = new RegisterRequest(mUserName, mEmailUser, mPassUser);
                registerRequest.setRequestCallBack(new ApiObjectCallBack<RegisterResponse>() {
                    @Override
                    public void onSuccess(RegisterResponse data) {
                        hideProgressBar();
                        if (data != null) {
                            loginApp();
                        } else {
                            DialogUtil.showDialog(getContext(), getString(R.string.error_server));
                        }
                    }

                    @Override
                    public void onFail(int failCode, String message) {
                        hideProgressBar();
                        CheckErrorCodeUtil.showDialogSpecialError(getContext(), failCode, message, APPConstant.API_REGISTER);
                    }
                });
                registerRequest.execute();
            }
        } else ToastUtil.makeText(getContext(), getString(R.string.no_connect_internet));
    }

    public void loginApp() {
        showProgressBar();
        if (NetworkUtils.getInstance(getContext()).isNetworkConnected()) {
            LoginRequest loginRequest = new LoginRequest(mEmailUser, mPassUser);
            loginRequest.setRequestCallBack(new ApiObjectCallBack<LoginResponse>() {
                @Override
                public void onSuccess(LoginResponse data) {
                    hideProgressBar();
                    if (data != null) {
                        SharedPrefUtils.saveUserInfo(data.userId, data.fullName, data.avatarImageUrl);
                        AppUtils.goToMainOrAdScreen(getActivity());
                    } else {
                        DialogUtil.showDialog(getContext(), getString(R.string.error_server));
                    }
                }

                @Override
                public void onFail(int failCode, String message) {
                    hideProgressBar();
                    CheckErrorCodeUtil.showDialogError(getContext(), failCode);
                }
            });
            loginRequest.execute();
        } else ToastUtil.makeText(getContext(), getString(R.string.no_connect_internet));
    }
}
