package vn.gmorunsystem.aimam.apis.request;

import com.android.volley.Request;

import java.util.HashMap;
import java.util.Map;

import vn.gmorunsystem.aimam.apis.response.SendItemReviewResponse;
import vn.gmorunsystem.aimam.apis.volley.core.ObjectApiRequest;
import vn.gmorunsystem.aimam.constants.APIConstant;

/**
 * Created by Veteran Commander on 6/12/2017.
 */

public class UpdateItemReviewRequest extends ObjectApiRequest<SendItemReviewResponse> {
    private int itemID;
    private float rating;
    private String content;
    private int reviewId;

    public UpdateItemReviewRequest(int itemID, float rating, String content, int reviewId) {
        this.itemID = itemID;
        this.rating = rating;
        this.content = content;
        this.reviewId = reviewId;
    }

    @Override
    public String getRequestURL() {
        return APIConstant.SEND_ITEM_REVIEW_URL + reviewId;
    }

    @Override
    public Map<String, String> getRequestParams() {
        Map<String, String> params = new HashMap<>();
        params.put(APIConstant.ITEM_ID, String.valueOf(itemID));
        params.put(APIConstant.VOTE, String.valueOf(rating));
        params.put(APIConstant.CONTENT, content);
        return params;
    }

    @Override
    public Class<SendItemReviewResponse> getResponseClass() {
        return SendItemReviewResponse.class;
    }

    @Override
    public int getMethod() {
        return Request.Method.PUT;
    }
}
