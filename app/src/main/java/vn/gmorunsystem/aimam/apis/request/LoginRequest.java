package vn.gmorunsystem.aimam.apis.request;

import com.android.volley.Request;

import java.util.HashMap;
import java.util.Map;

import vn.gmorunsystem.aimam.apis.response.LoginResponse;
import vn.gmorunsystem.aimam.apis.volley.core.ObjectApiRequest;
import vn.gmorunsystem.aimam.constants.APIConstant;

/**
 * Created by HuongTrung on 5/26/2017.
 */

public class LoginRequest extends ObjectApiRequest<LoginResponse> {

    private String userEmail;
    private String password;

    public LoginRequest(String userEmail, String password) {
        this.userEmail = userEmail;
        this.password = password;
    }

    @Override
    public String getRequestURL() {
        return APIConstant.LOGIN_URL;
    }

    @Override
    public Map<String, String> getRequestParams() {
        Map<String, String> params = new HashMap<>();
        params.put(APIConstant.EMAIL, userEmail);
        params.put(APIConstant.PASSWORD, password);
        return params;
    }

    @Override
    public Class<LoginResponse> getResponseClass() {
        return LoginResponse.class;
    }

    @Override
    public int getMethod() {
        return Request.Method.POST;
    }
}
