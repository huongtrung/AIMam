package vn.gmorunsystem.aimam.apis.request;

import com.android.volley.Request;

import java.util.HashMap;
import java.util.Map;

import vn.gmorunsystem.aimam.apis.response.ShopSearchResponse;
import vn.gmorunsystem.aimam.apis.volley.core.ObjectApiRequest;
import vn.gmorunsystem.aimam.constants.APIConstant;

/**
 * Created by HuongTrung on 6/19/2017.
 */

public class ShopSearchRequest extends ObjectApiRequest<ShopSearchResponse> {
    private String shopName;
    private Double latitude;
    private Double longitude;

    public ShopSearchRequest(String shopName, Double latitude, Double longitude) {
        this.shopName = shopName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String getRequestURL() {
        return APIConstant.GET_SHOP_SEARCH_URL;
    }

    @Override
    public Map<String, String> getRequestParams() {
        Map<String, String> params = new HashMap<>();
        params.put(APIConstant.KEYWORD, shopName);
        params.put(APIConstant.LATITUDE, String.valueOf(latitude));
        params.put(APIConstant.LONGITUDE, String.valueOf(longitude));
        return params;
    }

    @Override
    public Class<ShopSearchResponse> getResponseClass() {
        return ShopSearchResponse.class;
    }

    @Override
    public int getMethod() {
        return Request.Method.GET;
    }
}
