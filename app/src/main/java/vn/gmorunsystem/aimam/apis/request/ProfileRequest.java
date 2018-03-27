package vn.gmorunsystem.aimam.apis.request;

import com.android.volley.Request;

import java.util.Map;

import vn.gmorunsystem.aimam.apis.response.ProfileResponse;
import vn.gmorunsystem.aimam.apis.volley.core.ObjectApiRequest;
import vn.gmorunsystem.aimam.constants.APIConstant;
import vn.gmorunsystem.aimam.utils.SharedPrefUtils;

/**
 * Created by Veteran Commander on 6/8/2017.
 */

public class ProfileRequest extends ObjectApiRequest<ProfileResponse> {
    @Override
    public String getRequestURL() {
        return APIConstant.PROFILE_URL + SharedPrefUtils.getUserId();
    }

    @Override
    public Map<String, String> getRequestParams() {
        return null;
    }

    @Override
    public Class<ProfileResponse> getResponseClass() {
        return ProfileResponse.class;
    }

    @Override
    public int getMethod() {
        return Request.Method.GET;
    }
}
