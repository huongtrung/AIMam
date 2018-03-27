package vn.gmorunsystem.aimam.apis.volley.core;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

import vn.gmorunsystem.aimam.App;
import vn.gmorunsystem.aimam.apis.volley.callback.ApiObjectCallBack;
import vn.gmorunsystem.aimam.constants.APIConstant;
import vn.gmorunsystem.aimam.constants.APPConstant;
import vn.gmorunsystem.aimam.utils.NetworkUtils;
import vn.gmorunsystem.aimam.utils.SharedPrefUtils;

public abstract class ObjectApiRequest<T> extends BaseApiRequest<T> {

    public static final int ERROR_CODE = 999;

    String tag = null;

    private boolean suppressCommonApiError = false;

    public void setSuppressCommonApiError(boolean suppressCommonApiError) {
        this.suppressCommonApiError = suppressCommonApiError;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setDontCancel() {
        this.tag = NetworkUtils.DONT_CANCEL;
    }

    public void setIsLoadMore(boolean isLoadMore) {
        if (isLoadMore) {
            this.tag = NetworkUtils.LOAD_MORE;
        }
    }

    protected Map<String, String> handleRequestHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put(APPConstant.ACCESS_TOKEN, SharedPrefUtils.getAccessToken());
        headers.put(APPConstant.CLIENT, SharedPrefUtils.getClient());
        headers.put(APPConstant.UID, SharedPrefUtils.getUID());
        return headers;
    }

    @Override
    protected void excecuteRequest() {
        super.excecuteRequest();
        if ((getMethod() == Request.Method.GET || getMethod() == Request.Method.DELETE) && getRequestParams() != null) {
            baseTypeRequest = new GsonRequest<>(getMethod(), createRequestUrl(), getResponseClass(), handleRequestHeaders(), new HashMap<String, String>(), getListener(), getErrorListener());
        } else {
            baseTypeRequest = new GsonRequest<>(getMethod(), createRequestUrl(), getResponseClass(), handleRequestHeaders(), handleRequestParams(), getListener(), getErrorListener());
        }
        baseTypeRequest.setGsonRequestHeaderOnResult(this);
        baseTypeRequest.setRetryPolicy(getDefaultRetryPolicy());
        if (tag != null) {
            baseTypeRequest.setTag(this.tag);
        }
        NetworkUtils.getInstance(App.getInstance()).addToRequestQueue(baseTypeRequest);
    }

    public ApiObjectCallBack<T> requestCallBack;

    public void setRequestCallBack(ApiObjectCallBack<T> requestCallBack) {
        this.requestCallBack = requestCallBack;
    }

    public void handleResponse(T response) {
        if (requestCallBack != null) {
            requestCallBack.onSuccess(response);
        }

    }

    @Override
    public void onRequestSuccess(T response) {
        handleResponse(response);
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
            String message;
            NetworkResponse response = error.networkResponse;
            if (response != null && response.data != null) {
                message = new String(response.data);
                if (response.headers != null) {
                    SharedPrefUtils.saveCurrentAccess(response.headers);
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

    public String getErrorDialogTitle() {
        return null;
    }

    public boolean suppressCommonApiError() {
        return suppressCommonApiError;
    }

    @Override
    protected boolean isHideConnectionErrorDialog() {
        return suppressCommonApiError() || super.isHideConnectionErrorDialog();
    }

    @Override
    protected boolean isHideApiErrorDialog() {
        return suppressCommonApiError() || super.isHideApiErrorDialog();
    }

}
