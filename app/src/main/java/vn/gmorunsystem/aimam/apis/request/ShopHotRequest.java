package vn.gmorunsystem.aimam.apis.request;

import com.android.volley.Request;

import java.util.HashMap;
import java.util.Map;

import vn.gmorunsystem.aimam.apis.response.ShopHotResponse;
import vn.gmorunsystem.aimam.apis.volley.core.ObjectApiRequest;
import vn.gmorunsystem.aimam.constants.APIConstant;

/**
 * Created by HuongTrung on 6/5/2017.
 */

public class ShopHotRequest extends ObjectApiRequest<ShopHotResponse> {
    private int shopId;

    public ShopHotRequest(int shopId) {
        this.shopId = shopId;
    }

    @Override
    public String getRequestURL() {
        return APIConstant.SHOP_HOT_URL;
    }

    @Override
    public Map<String, String> getRequestParams() {
        Map<String, String> params = new HashMap<>();
        params.put(APIConstant.SHOP_ID, String.valueOf(shopId));
        return params;
    }

    @Override
    public Class<ShopHotResponse> getResponseClass() {
        return ShopHotResponse.class;
    }

    @Override
    public int getMethod() {
        return Request.Method.GET;
    }
}
