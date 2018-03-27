package vn.gmorunsystem.aimam.apis.request;

import com.android.volley.Request;

import java.util.Map;

import vn.gmorunsystem.aimam.apis.response.NewFeedResponse;
import vn.gmorunsystem.aimam.apis.volley.core.ObjectApiRequest;
import vn.gmorunsystem.aimam.constants.APIConstant;

/**
 * Created by HuongTrung on 09/01/2017.
 */

public class NewFeedRequest extends ObjectApiRequest<NewFeedResponse> {
    private int page;

    public NewFeedRequest(int page) {
        this.page = page;
    }

    @Override
    public String getRequestURL() {
        return APIConstant.NEW_FEED_URL + page;
    }

    @Override
    public Map<String, String> getRequestParams() {
        return null;
    }

    @Override
    public Class<NewFeedResponse> getResponseClass() {
        return NewFeedResponse.class;
    }

    @Override
    public int getMethod() {
        return Request.Method.GET;
    }
}
