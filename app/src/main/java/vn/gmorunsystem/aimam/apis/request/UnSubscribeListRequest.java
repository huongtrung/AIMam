package vn.gmorunsystem.aimam.apis.request;

import com.android.volley.Request;

import java.util.Map;

import vn.gmorunsystem.aimam.apis.response.UnSubscribeResponse;
import vn.gmorunsystem.aimam.apis.volley.core.ObjectApiRequest;
import vn.gmorunsystem.aimam.constants.APIConstant;

/**
 * Created by Veteran Commander on 6/5/2017.
 */

public class UnSubscribeListRequest extends ObjectApiRequest<UnSubscribeResponse> {

    private int shopId;

    public UnSubscribeListRequest(int id) {
        shopId = id;
    }

    @Override
    public String getRequestURL() {
        return APIConstant.UN_SUBSCRIBED_URL + shopId;
    }

    public Map<String, String> getRequestParams() {
        return null;
    }

    @Override
    public Class<UnSubscribeResponse> getResponseClass() {
        return UnSubscribeResponse.class;
    }

    @Override
    public int getMethod() {
        return Request.Method.DELETE;
    }
}
