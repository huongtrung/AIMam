package vn.gmorunsystem.aimam.apis.request;

import com.android.volley.Request;

import java.util.HashMap;
import java.util.Map;

import vn.gmorunsystem.aimam.apis.response.ShopStampResponse;
import vn.gmorunsystem.aimam.apis.volley.core.ObjectApiRequest;
import vn.gmorunsystem.aimam.constants.APIConstant;

/**
 * Created by HuongTrung on 6/14/2017.
 */

public class ShopStampRequest extends ObjectApiRequest<ShopStampResponse> {
    private int shopId;

    public ShopStampRequest(int shopId) {
        this.shopId = shopId;
    }

    @Override
    public String getRequestURL() {
        return APIConstant.GET_SHOP_STAMP_URL;
    }

    @Override
    public Map<String, String> getRequestParams() {
        Map<String, String> params = new HashMap<>();
        params.put(APIConstant.SHOP_ID, String.valueOf(shopId));
        return params;
    }

    @Override
    public Class<ShopStampResponse> getResponseClass() {
        return ShopStampResponse.class;
    }

    @Override
    public int getMethod() {
        return Request.Method.GET;
    }
}
