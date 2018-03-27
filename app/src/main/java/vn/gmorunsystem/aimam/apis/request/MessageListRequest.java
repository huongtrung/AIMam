package vn.gmorunsystem.aimam.apis.request;

import com.android.volley.Request;

import java.util.Map;

import vn.gmorunsystem.aimam.apis.response.MessageListResponse;
import vn.gmorunsystem.aimam.apis.volley.core.ObjectApiRequest;
import vn.gmorunsystem.aimam.constants.APIConstant;

/**
 * Created by HuongTrung on 6/12/2017.
 */

public class MessageListRequest extends ObjectApiRequest<MessageListResponse> {
    private int page;

    public MessageListRequest(int page) {
        this.page = page;
    }

    @Override
    public String getRequestURL() {
        return APIConstant.GET_MESSAGE_LIST_URL + page;
    }

    @Override
    public Map<String, String> getRequestParams() {
        return null;
    }

    @Override
    public Class<MessageListResponse> getResponseClass() {
        return MessageListResponse.class;
    }

    @Override
    public int getMethod() {
        return Request.Method.GET;
    }
}
