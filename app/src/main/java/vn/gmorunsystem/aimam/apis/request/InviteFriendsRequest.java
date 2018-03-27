package vn.gmorunsystem.aimam.apis.request;

import com.android.volley.Request;

import java.util.HashMap;
import java.util.Map;

import vn.gmorunsystem.aimam.apis.response.InviteFriendResponse;
import vn.gmorunsystem.aimam.apis.volley.core.ObjectApiRequest;
import vn.gmorunsystem.aimam.constants.APIConstant;

/**
 * Created by HuongTrung on 6/1/2017.
 */

public class InviteFriendsRequest extends ObjectApiRequest<InviteFriendResponse> {
    private String email;

    public InviteFriendsRequest(String email) {
        this.email = email;
    }

    @Override
    public String getRequestURL() {
        return APIConstant.INVITE_FRIEND_URL;
    }

    @Override
    public Map<String, String> getRequestParams() {
        Map<String, String> params = new HashMap<>();
        params.put(APIConstant.EMAIL, email);
        return params;
    }

    @Override
    public Class<InviteFriendResponse> getResponseClass() {
        return InviteFriendResponse.class;
    }

    @Override
    public int getMethod() {
        return Request.Method.POST;
    }

}
