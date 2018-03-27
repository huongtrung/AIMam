package vn.gmorunsystem.aimam.apis.request;

import com.android.volley.Request;

import java.util.Map;

import vn.gmorunsystem.aimam.apis.response.BlankResponse;
import vn.gmorunsystem.aimam.apis.volley.core.ObjectApiRequest;
import vn.gmorunsystem.aimam.constants.APIConstant;

/**
 * Created by Veteran Commander on 6/12/2017.
 */

public class DeleteItemReviewRequest extends ObjectApiRequest<BlankResponse> {
    private int reviewId;

    public DeleteItemReviewRequest(int reviewId) {
        this.reviewId = reviewId;
    }

    @Override
    public String getRequestURL() {
        return APIConstant.SEND_ITEM_REVIEW_URL + reviewId;
    }

    @Override
    public Map<String, String> getRequestParams() {
        return null;
    }

    @Override
    public Class<BlankResponse> getResponseClass() {
        return BlankResponse.class;
    }

    @Override
    public int getMethod() {
        return Request.Method.DELETE;
    }
}
