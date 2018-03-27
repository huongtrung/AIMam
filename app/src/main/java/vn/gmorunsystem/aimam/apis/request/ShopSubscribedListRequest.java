package vn.gmorunsystem.aimam.apis.request;

import com.android.volley.Request;

import java.util.Map;

import vn.gmorunsystem.aimam.apis.response.ShopSubscribedListResponse;
import vn.gmorunsystem.aimam.apis.volley.core.ObjectApiRequest;
import vn.gmorunsystem.aimam.constants.APIConstant;

/**
 * Created by Veteran Commander on 6/5/2017.
 */

public class ShopSubscribedListRequest extends ObjectApiRequest<ShopSubscribedListResponse> {
    @Override
    public String getRequestURL() {
        return APIConstant.GET_SHOP_SUBSCRIBE_URL;
    }

    @Override
    public Map<String, String> getRequestParams() {
        return null;
    }

    @Override
    public Class<ShopSubscribedListResponse> getResponseClass() {
        return ShopSubscribedListResponse.class;
    }

    @Override
    public int getMethod() {
        return Request.Method.GET;
    }
}
