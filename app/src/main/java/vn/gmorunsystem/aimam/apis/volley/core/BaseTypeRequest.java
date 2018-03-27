package vn.gmorunsystem.aimam.apis.volley.core;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;

import java.util.Map;


public abstract class BaseTypeRequest<T> extends Request<T> {

    protected final Class<T> clazz;
    protected final Response.Listener<T> listener;
    protected final Map<String, String> header;
    protected final Map<String, String> params;
    protected Map<String, String> responseHeader;
    protected GsonRequestHeaderOnResult gsonRequestHeaderOnResult;

    public BaseTypeRequest(int method, String url, Response.ErrorListener errorListener, Class<T> clazz, Response.Listener<T> listener, Map<String, String> header, Map<String, String> params) {
        super(method, url, errorListener);
        this.clazz = clazz;
        this.listener = listener;
        this.header = header;
        this.params = params;
    }

    public void updateParam(String key, String value) {
        if (params != null) {
            params.put(key, value);
        }
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return header != null ? header : super.getHeaders();
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }

    public void setGsonRequestHeaderOnResult(GsonRequestHeaderOnResult mGsonRequestHeaderOnResult) {
        this.gsonRequestHeaderOnResult = mGsonRequestHeaderOnResult;
    }

    public interface GsonRequestHeaderOnResult {
        void onGsonRequestHeaderResult(Map<String, String> mHeaders);
    }

}
