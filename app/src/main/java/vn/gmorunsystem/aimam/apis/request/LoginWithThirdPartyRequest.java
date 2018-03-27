package vn.gmorunsystem.aimam.apis.request;

import com.android.volley.Request;

import java.util.HashMap;
import java.util.Map;

import vn.gmorunsystem.aimam.apis.response.LoginResponse;
import vn.gmorunsystem.aimam.apis.volley.core.ObjectApiRequest;
import vn.gmorunsystem.aimam.constants.APIConstant;
import vn.gmorunsystem.aimam.constants.APPConstant;
import vn.gmorunsystem.aimam.utils.StringUtil;

/**
 * Created by adm on 7/24/2017.
 */

public class LoginWithThirdPartyRequest extends ObjectApiRequest<LoginResponse> {
    private String provider;
    private String uid;
    private String email;
    private String fullName;
    private String avatarUrl;
    private Integer sex;
    private String birthDay;


    public LoginWithThirdPartyRequest(String provider, String uid, String email, String fullName, String avatarUrl, int sex, String birthDay) {
        this.provider = provider;
        this.uid = uid;
        this.email = email;
        this.fullName = fullName;
        this.avatarUrl = avatarUrl;
        this.sex = sex;
        this.birthDay = birthDay;
    }

    @Override
    public String getRequestURL() {
        return APIConstant.REGISTER_BY_THIRD_PARTY;
    }

    @Override
    public Map<String, String> getRequestParams() {
        Map<String, String> params = new HashMap<>();
        params.put(APIConstant.PROVIDER, provider);
        params.put(APPConstant.UID, uid);
        if (!StringUtil.isEmpty(email)) {
            params.put(APIConstant.EMAIL, email);
        }
        if (!StringUtil.isEmpty(birthDay)){
            params.put(APIConstant.BIRTHDAY,birthDay);
        }
        if (sex!=null){
            params.put(APIConstant.SEX,sex.toString());
        }
        params.put(APIConstant.FULL_NAME, fullName);
        params.put(APIConstant.AVATAR, avatarUrl);
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

    @Override
    protected Map<String, String> handleRequestHeaders() {
        return null;
    }
}
