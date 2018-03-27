package vn.gmorunsystem.aimam.apis.request;

import com.android.volley.Request;

import java.util.Map;

import vn.gmorunsystem.aimam.apis.volley.core.ObjectApiRequest;
import vn.gmorunsystem.aimam.bean.data.MessageListData;
import vn.gmorunsystem.aimam.constants.APIConstant;

/**
 * Created by HuongTrung on 6/12/2017.
 */

public class MessageDetailRequest extends ObjectApiRequest<MessageListData> {
    private int messageId;

    public MessageDetailRequest(int messageId) {
        this.messageId = messageId;
    }

    @Override
    public String getRequestURL() {
        return APIConstant.GET_MESSAGE_DETAIL_URL + messageId;
    }

    @Override
    public Map<String, String> getRequestParams() {
        return null;
    }

    @Override
    public Class<MessageListData> getResponseClass() {
        return MessageListData.class;
    }

    @Override
    public int getMethod() {
        return Request.Method.GET;
    }
}
