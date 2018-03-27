package vn.gmorunsystem.aimam.apis.request;

import com.android.volley.Request;

import java.util.HashMap;
import java.util.Map;

import vn.gmorunsystem.aimam.apis.response.UsefulResponse;
import vn.gmorunsystem.aimam.apis.volley.core.ObjectApiRequest;
import vn.gmorunsystem.aimam.constants.APIConstant;

/**
 * Created by adm on 7/4/2017.
 */

public class UsefulRequest extends ObjectApiRequest<UsefulResponse> {
    private int reviewId;

    public UsefulRequest(int reviewId) {
        this.reviewId = reviewId;
    }

    @Override
    public String getRequestURL() {
        return APIConstant.USEFUL_URL;
    }

    @Override
    public Map<String, String> getRequestParams() {
        Map<String, String> params = new HashMap<>();
        params.put(APIConstant.REVIEW_ID, String.valueOf(reviewId));
        return params;
    }

    @Override
    public Class<UsefulResponse> getResponseClass() {
        return UsefulResponse.class;
    }

    @Override
    public int getMethod() {
        return Request.Method.POST;
    }
}
