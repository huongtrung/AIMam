package vn.gmorunsystem.aimam.apis.request;

import com.android.volley.Request;

import java.util.HashMap;
import java.util.Map;

import vn.gmorunsystem.aimam.apis.response.SubscribedResponse;
import vn.gmorunsystem.aimam.apis.volley.core.ObjectApiRequest;
import vn.gmorunsystem.aimam.constants.APIConstant;

/**
 * Created by HuongTrung on 6/8/2017.
 */

public class SubscribedRequest extends ObjectApiRequest<SubscribedResponse> {
    private int shopId;

    public SubscribedRequest(int shopId) {
        this.shopId = shopId;
    }

    @Override
    public String getRequestURL() {
        return APIConstant.SUBSCRIBED_URL;
    }

    @Override
    public Map<String, String> getRequestParams() {
        Map<String, String> params = new HashMap<>();
        params.put(APIConstant.SHOP_ID, String.valueOf(shopId));
        return params;
    }

    @Override
    public Class<SubscribedResponse> getResponseClass() {
        return SubscribedResponse.class;
    }

    @Override
    public int getMethod() {
        return Request.Method.POST;
    }
}
