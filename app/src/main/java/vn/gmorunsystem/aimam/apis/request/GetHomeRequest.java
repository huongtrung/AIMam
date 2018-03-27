package vn.gmorunsystem.aimam.apis.request;

import com.android.volley.Request;

import java.util.HashMap;
import java.util.Map;

import vn.gmorunsystem.aimam.apis.response.GetHomeResponse;
import vn.gmorunsystem.aimam.apis.volley.core.ObjectApiRequest;
import vn.gmorunsystem.aimam.constants.APIConstant;
import vn.gmorunsystem.aimam.utils.SharedPrefUtils;

/**
 * Created by Veteran Commander on 6/14/2017.
 */

public class GetHomeRequest extends ObjectApiRequest<GetHomeResponse> {
    private static final int DEFAULT_LIMIT_SHOP = 9;
    private Double latitude;
    private Double longitude;

    public GetHomeRequest(Double latitude, Double longtitude) {
        this.latitude = latitude;
        this.longitude = longtitude;
    }

    @Override
    public String getRequestURL() {
        return APIConstant.HOME_URL;
    }

    @Override
    public Map<String, String> getRequestParams() {
        Map<String, String> params = new HashMap<>();
        params.put(APIConstant.USER_ID, String.valueOf(SharedPrefUtils.getUserId()));
        params.put(APIConstant.LATITUDE, String.valueOf(latitude));
        params.put(APIConstant.LONGITUDE, String.valueOf(longitude));
        params.put(APIConstant.LIMIT_SHOP, String.valueOf(DEFAULT_LIMIT_SHOP));
        return params;
    }

    @Override
    public Class<GetHomeResponse> getResponseClass() {
        return GetHomeResponse.class;
    }

    @Override
    public int getMethod() {
        return Request.Method.GET;
    }

}
