package vn.gmorunsystem.aimam.apis.request;

import com.android.volley.Request;

import java.util.HashMap;
import java.util.Map;

import vn.gmorunsystem.aimam.apis.response.SendDeviceInfoResponse;
import vn.gmorunsystem.aimam.apis.volley.core.ObjectApiRequest;
import vn.gmorunsystem.aimam.constants.APIConstant;

/**
 * Created by HuongTrung on 09/15/2017.
 */

public class SendDeviceInfoRequest extends ObjectApiRequest<SendDeviceInfoResponse> {
    private int userId;
    private String deviceId;
    private String fireBase_key;

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setFireBase_key(String fireBase_key) {
        this.fireBase_key = fireBase_key;
    }

    @Override
    public String getRequestURL() {
        return APIConstant.SEND_DEVICE_INFO_URL;
    }

    @Override
    public Map<String, String> getRequestParams() {
        Map<String, String> param = new HashMap<>();
        param.put(APIConstant.USER_ID, String.valueOf(userId));
        param.put(APIConstant.DEVICE_TYPE, APIConstant.TYPE_ANDROID);
        param.put(APIConstant.FIREBASE_KEY, fireBase_key);
        param.put(APIConstant.DEVICE_UID, deviceId);
        return param;
    }

    @Override
    public Class<SendDeviceInfoResponse> getResponseClass() {
        return SendDeviceInfoResponse.class;
    }

    @Override
    public int getMethod() {
        return Request.Method.POST;
    }
}
