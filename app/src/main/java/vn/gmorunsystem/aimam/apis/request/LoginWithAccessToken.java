package vn.gmorunsystem.aimam.apis.request;

import com.android.volley.Request;

import java.util.Map;

import vn.gmorunsystem.aimam.apis.response.LoginResponse;
import vn.gmorunsystem.aimam.apis.volley.core.ObjectApiRequest;
import vn.gmorunsystem.aimam.constants.APIConstant;

/**
 * Created by HuongTrung on 08/08/2017.
 */

public class LoginWithAccessToken extends ObjectApiRequest<LoginResponse> {
    @Override
    public String getRequestURL() {
        return APIConstant.LOGIN_URL;
    }

    @Override
    public Map<String, String> getRequestParams() {
        return null;
    }

    @Override
    public Class<LoginResponse> getResponseClass() {
        return LoginResponse.class;
    }

    @Override
    public int getMethod() {
        return Request.Method.GET;
    }
}
