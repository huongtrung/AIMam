package vn.gmorunsystem.aimam.apis.request;

import com.android.volley.Request;

import java.util.Map;

import vn.gmorunsystem.aimam.apis.response.GetShopResponse;
import vn.gmorunsystem.aimam.apis.volley.core.ObjectApiRequest;
import vn.gmorunsystem.aimam.constants.APIConstant;

/**
 * Created by HuongTrung on 6/6/2017.
 */

public class GetShopRequest extends ObjectApiRequest<GetShopResponse> {

    private int shopId;

    public GetShopRequest(int shopId) {
        this.shopId = shopId;
    }

    @Override
    public String getRequestURL() {
        return APIConstant.GET_SHOP_URL + shopId;
    }

    @Override
    public Map<String, String> getRequestParams() {
        return null;
    }

    @Override
    public Class<GetShopResponse> getResponseClass() {
        return GetShopResponse.class;
    }

    @Override
    public int getMethod() {
        return Request.Method.GET;
    }
}
