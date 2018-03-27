package vn.gmorunsystem.aimam.apis.request;

import com.android.volley.Request;

import java.util.Map;

import vn.gmorunsystem.aimam.apis.response.ShopReviewResponse;
import vn.gmorunsystem.aimam.apis.volley.core.ObjectApiRequest;
import vn.gmorunsystem.aimam.constants.APIConstant;

/**
 * Created by tiena on 6/27/2017.
 */

public class GetShopReviewRequest extends ObjectApiRequest<ShopReviewResponse> {
    int shopId;
    private int page = 1;

    public GetShopReviewRequest(int shopId, int pageNum) {
        this.shopId = shopId;
        this.page = pageNum;
    }

    @Override
    public String getRequestURL() {
        return APIConstant.SHOP_REVIEW_URL + shopId + "&page=" + page;
    }

    @Override
    public Class<ShopReviewResponse> getResponseClass() {
        return ShopReviewResponse.class;
    }

    @Override
    public Map<String, String> getRequestParams() {
        return null;
    }


    @Override
    public int getMethod() {
        return Request.Method.GET;
    }
}
