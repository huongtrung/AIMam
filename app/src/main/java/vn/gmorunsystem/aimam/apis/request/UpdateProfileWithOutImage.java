package vn.gmorunsystem.aimam.apis.request;

import com.android.volley.Request;

import java.util.Map;

import vn.gmorunsystem.aimam.apis.response.BlankResponse;
import vn.gmorunsystem.aimam.apis.volley.core.ObjectApiRequest;
import vn.gmorunsystem.aimam.constants.APIConstant;
import vn.gmorunsystem.aimam.utils.SharedPrefUtils;

/**
 * Created by Veteran Commander on 6/21/2017.
 */

public class UpdateProfileWithOutImage extends ObjectApiRequest<BlankResponse> {
    Map<String, String> params;

    public UpdateProfileWithOutImage(Map<String, String> params) {
        this.params = params;
    }

    @Override
    public String getRequestURL() {
        return APIConstant.UPDATE_PROFILE_URL + SharedPrefUtils.getUserId();
    }

    @Override
    public Map<String, String> getRequestParams() {
        return params;
    }

    @Override
    public Class<BlankResponse> getResponseClass() {
        return BlankResponse.class;
    }

    @Override
    public int getMethod() {
        return Request.Method.PUT;
    }
}
