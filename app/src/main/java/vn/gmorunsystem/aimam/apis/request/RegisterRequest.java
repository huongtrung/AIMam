package vn.gmorunsystem.aimam.apis.request;

import com.android.volley.Request;

import java.util.HashMap;
import java.util.Map;

import vn.gmorunsystem.aimam.apis.response.RegisterResponse;
import vn.gmorunsystem.aimam.apis.volley.core.ObjectApiRequest;
import vn.gmorunsystem.aimam.constants.APIConstant;

/**
 * Created by Veteran Commander on 5/29/2017.
 */

public class RegisterRequest extends ObjectApiRequest<RegisterResponse> {

    private String userName;
    private String emailUser;
    private String passUser;

    public RegisterRequest(String userName, String emailUser, String passUser) {
        this.userName = userName;
        this.emailUser = emailUser;
        this.passUser = passUser;
    }

    @Override
    public String getRequestURL() {
        return APIConstant.REGISTER_URL;
    }

    @Override
    public Map<String, String> getRequestParams() {
        Map<String, String> params = new HashMap<>();
        params.put(APIConstant.FULL_NAME, userName);
        params.put(APIConstant.EMAIL, emailUser);
        params.put(APIConstant.PASSWORD, passUser);
        return params;
    }

    @Override
    public Class<RegisterResponse> getResponseClass() {
        return RegisterResponse.class;
    }

    @Override
    public int getMethod() {
        return Request.Method.POST;
    }


}
