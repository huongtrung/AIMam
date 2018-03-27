package vn.gmorunsystem.aimam.apis.request;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import vn.gmorunsystem.aimam.apis.response.BlankResponse;
import vn.gmorunsystem.aimam.apis.volley.callback.ApiObjectCallBack;
import vn.gmorunsystem.aimam.apis.volley.core.UploadBinaryApiRequest;
import vn.gmorunsystem.aimam.constants.APIConstant;
import vn.gmorunsystem.aimam.constants.APPConstant;
import vn.gmorunsystem.aimam.utils.AppUtils;
import vn.gmorunsystem.aimam.utils.SharedPrefUtils;

/**
 * Created by Veteran Commander on 6/20/2017.
 */

public class UploadImageRequest extends UploadBinaryApiRequest<BlankResponse> {
    private static final int ERROR_CODE = 999;
    private ApiObjectCallBack<BlankResponse> requestCallBack;

    Map<String, String> params;


    public UploadImageRequest(Map<String, String> params, Map<String, File> fileMap) {
        this.params = params;
        setRequestFiles(fileMap);
    }

    public void setRequestCallBack(ApiObjectCallBack<BlankResponse> requestCallBack) {
        this.requestCallBack = requestCallBack;
    }

    //extend upload binary request ko phai object api nen dung xoa getheader nay
    @Override
    public Map<String, String> getRequestHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put(APPConstant.ACCESS_TOKEN, SharedPrefUtils.getAccessToken());
        headers.put(APPConstant.CLIENT, SharedPrefUtils.getClient());
        headers.put(APPConstant.UID, SharedPrefUtils.getUID());
        return headers;
    }

    @Override
    public String getRequestURL() {
        return APIConstant.IMG_UPLOAD_URL;
    }

    @Override
    public Map<String, String> getRequestParams() {
        return params;
    }

    @Override
    public Class<BlankResponse> getResponseClass() {
        return BlankResponse.class;
    }

    @Override
    public int getMethod() {
        return Request.Method.POST;
    }

    @Override
    public void onRequestSuccess(BlankResponse response) {
        if (requestCallBack != null) {
            requestCallBack.onSuccess(response);
        }
    }

    @Override
    public void onRequestError(VolleyError error) {
        if (requestCallBack != null) {
            int errorCode = ERROR_CODE;
            if (error.networkResponse != null) {
                errorCode = error.networkResponse.statusCode;
            }
            if (error instanceof ParseError) {
                errorCode = APIConstant.ERROR_CODE_998_JSON_SYNTAX;
            } else if (error instanceof TimeoutError) {
                errorCode = APIConstant.ERROR_CODE_999;
            }
            String message = null;
            NetworkResponse response = error.networkResponse;
            if (response != null && response.data != null) {
                message = new String(response.data);
                if (message.contains("errors")) {
                    message = AppUtils.trimMessage(message, "errors");
                    if (response.headers != null) {
                        SharedPrefUtils.saveCurrentAccess(response.headers);
                    }
                }
            } else {
                message = error.getMessage();
            }
            requestCallBack.onFail(errorCode, message);
        }
    }

    @Override
    public void handleResponseHeader(Map<String, String> headers) {
        if (headers != null && headers.containsKey(APPConstant.ACCESS_TOKEN) && headers.containsKey(APPConstant.CLIENT) && headers.containsKey(APPConstant.UID)) {
            SharedPrefUtils.saveCurrentAccess(headers);
        }
    }
}
