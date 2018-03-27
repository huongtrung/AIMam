package vn.gmorunsystem.aimam.apis.request;

import com.android.volley.Request;

import java.util.Map;

import vn.gmorunsystem.aimam.apis.response.ShopMenuResponse;
import vn.gmorunsystem.aimam.apis.volley.core.ObjectApiRequest;
import vn.gmorunsystem.aimam.constants.APIConstant;

/**
 * Created by HuongTrung on 6/5/2017.
 */

public class ShopMenuRequest extends ObjectApiRequest<ShopMenuResponse> {
    private int shopId;
    private int page;

    public ShopMenuRequest(int shopId, int page) {
        this.shopId = shopId;
        this.page = page;
    }

    @Override
    public String getRequestURL() {
        return APIConstant.SHOP_MENU_URL + "shop_id=" + shopId + "&page=" + page;
    }

    @Override
    public Map<String, String> getRequestParams() {
        return null;
    }

    @Override
    public Class<ShopMenuResponse> getResponseClass() {
        return ShopMenuResponse.class;
    }

    @Override
    public int getMethod() {
        return Request.Method.GET;
    }
}
