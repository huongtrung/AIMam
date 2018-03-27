package vn.gmorunsystem.aimam.apis.request;

import com.android.volley.Request;

import java.util.HashMap;
import java.util.Map;

import vn.gmorunsystem.aimam.apis.response.ShopStampGiftResponse;
import vn.gmorunsystem.aimam.apis.volley.core.ObjectApiRequest;
import vn.gmorunsystem.aimam.constants.APIConstant;

/**
 * Created by HuongTrung on 6/14/2017.
 */

public class ShopStampGiftRequest extends ObjectApiRequest<ShopStampGiftResponse> {
    private int shopId;
    private int position;

    public ShopStampGiftRequest(int shopId, int position) {
        this.shopId = shopId;
        this.position = position;
    }

    @Override
    public String getRequestURL() {
        return APIConstant.GET_SHOP_STAMP_URL;
    }

    @Override
    public Map<String, String> getRequestParams() {
        Map<String, String> params = new HashMap<>();
        params.put(APIConstant.SHOP_ID, String.valueOf(shopId));
        params.put(APIConstant.STAMP_POSITION, String.valueOf(position));
        return params;
    }

    @Override
    public Class<ShopStampGiftResponse> getResponseClass() {
        return ShopStampGiftResponse.class;
    }

    @Override
    public int getMethod() {
        return Request.Method.POST;
    }
}
