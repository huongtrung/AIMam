package vn.gmorunsystem.aimam.apis.request;

import com.android.volley.Request;

import java.util.Map;

import vn.gmorunsystem.aimam.apis.response.GetNearestShopResponse;
import vn.gmorunsystem.aimam.apis.volley.core.ObjectApiRequest;
import vn.gmorunsystem.aimam.constants.APIConstant;

/**
 * Created by adm on 8/30/2017.
 */

public class GetNearestShopRequest extends ObjectApiRequest<GetNearestShopResponse> {
    String realUrl;

    public GetNearestShopRequest(Double latitude, Double longitude) {
        realUrl = APIConstant.SHOP_NEAREST + "lat=" + latitude + "&lon=" + longitude;
    }

    public GetNearestShopRequest(Integer shopId) {
        realUrl = APIConstant.SHOP_NEAREST + "shop_id=" + shopId;
    }

    @Override
    public String getRequestURL() {
        return realUrl;
    }

    @Override
    public Map<String, String> getRequestParams() {
        return null;
    }

    @Override
    public Class<GetNearestShopResponse> getResponseClass() {
        return GetNearestShopResponse.class;
    }

    @Override
    public int getMethod() {
        return Request.Method.GET;
    }
}
