package vn.gmorunsystem.aimam.apis.request;

import com.android.volley.Request;

import java.util.HashMap;
import java.util.Map;

import vn.gmorunsystem.aimam.apis.response.FeedBackResponse;
import vn.gmorunsystem.aimam.apis.volley.core.ObjectApiRequest;
import vn.gmorunsystem.aimam.constants.APIConstant;

/**
 * Created by HuongTrung on 6/2/2017.
 */

public class FeedBackRequest extends ObjectApiRequest<FeedBackResponse> {
    private String contentFeedback;

    public FeedBackRequest(String contentFeedback) {
        this.contentFeedback = contentFeedback;
    }

    @Override
    public String getRequestURL() {
        return APIConstant.FEEDBACK_URL;
    }

    @Override
    public Map<String, String> getRequestParams() {
        Map<String, String> params = new HashMap<>();
        params.put(APIConstant.CONTENT, contentFeedback);
        return params;
    }

    @Override
    public Class<FeedBackResponse> getResponseClass() {
        return FeedBackResponse.class;
    }

    @Override
    public int getMethod() {
        return Request.Method.POST;
    }
}
