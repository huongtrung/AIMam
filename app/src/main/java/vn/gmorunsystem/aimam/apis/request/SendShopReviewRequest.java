package vn.gmorunsystem.aimam.apis.request;

import com.android.volley.Request;

import java.util.HashMap;
import java.util.Map;

import vn.gmorunsystem.aimam.apis.response.SendShopReviewResponse;
import vn.gmorunsystem.aimam.apis.volley.core.ObjectApiRequest;
import vn.gmorunsystem.aimam.constants.APIConstant;

/**
 * Created by adm on 6/28/2017.
 */

public class SendShopReviewRequest extends ObjectApiRequest<SendShopReviewResponse> {
    int shopId;
    private int rating;
    private String content;

    public SendShopReviewRequest(int shopId, int rating, String content) {
        this.shopId = shopId;
        this.rating = rating;
        this.content = content;
    }

    @Override
    public String getRequestURL() {
        return APIConstant.SEND_SHOP_REVIEW_URL;
    }

    @Override
    public Map<String, String> getRequestParams() {
        Map<String, String> params = new HashMap<>();
        params.put(APIConstant.SHOP_ID, String.valueOf(shopId));
        params.put(APIConstant.VOTE, String.valueOf(rating));
        params.put(APIConstant.CONTENT, content);
        return params;
    }

    @Override
    public Class<SendShopReviewResponse> getResponseClass() {
        return SendShopReviewResponse.class;
    }

    @Override
    public int getMethod() {
        return Request.Method.POST;
    }
}
