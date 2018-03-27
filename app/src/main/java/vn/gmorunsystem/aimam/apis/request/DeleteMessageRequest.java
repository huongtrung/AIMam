package vn.gmorunsystem.aimam.apis.request;

import com.android.volley.Request;

import java.util.Map;

import vn.gmorunsystem.aimam.apis.response.DeleteMessageResponse;
import vn.gmorunsystem.aimam.apis.volley.core.ObjectApiRequest;
import vn.gmorunsystem.aimam.constants.APIConstant;

/**
 * Created by HuongTrung on 06/26/2017.
 */

public class DeleteMessageRequest extends ObjectApiRequest<DeleteMessageResponse> {
    private int messageId;

    public DeleteMessageRequest(int messageId) {
        this.messageId = messageId;
    }

    @Override
    public String getRequestURL() {
        return APIConstant.DELETE_MESSAGE_URL + messageId;
    }

    @Override
    public Map<String, String> getRequestParams() {
        return null;
    }

    @Override
    public Class<DeleteMessageResponse> getResponseClass() {
        return DeleteMessageResponse.class;
    }

    @Override
    public int getMethod() {
        return Request.Method.DELETE;
    }
}
