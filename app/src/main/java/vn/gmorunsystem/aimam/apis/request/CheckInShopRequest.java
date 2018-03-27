package vn.gmorunsystem.aimam.apis.request;

import com.android.volley.Request;

import java.util.HashMap;
import java.util.Map;

import vn.gmorunsystem.aimam.apis.response.CheckInShopResponse;
import vn.gmorunsystem.aimam.apis.volley.core.ObjectApiRequest;
import vn.gmorunsystem.aimam.constants.APIConstant;

/**
 * Created by HuongTrung on 07/05/2017.
 */

public class CheckInShopRequest extends ObjectApiRequest<CheckInShopResponse> {
    private int shopId;
    private double lat;
    private double lon;

    public CheckInShopRequest(int shopId, double lat, double lon) {
        this.shopId = shopId;
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    public String getRequestURL() {
        return APIConstant.CHECK_IN_URL;
    }

    @Override
    public Map<String, String> getRequestParams() {
        Map<String, String> params = new HashMap<>();
        params.put(APIConstant.SHOP_ID, String.valueOf(shopId));
        params.put(APIConstant.LATITUDE, String.valueOf(lat));
        params.put(APIConstant.LONGITUDE_STAMP, String.valueOf(lon));
        return params;
    }

    @Override
    public Class<CheckInShopResponse> getResponseClass() {
        return CheckInShopResponse.class;
    }

    @Override
    public int getMethod() {
        return Request.Method.POST;
    }
}
