package vn.gmorunsystem.aimam.apis.volley.core;

import vn.gmorunsystem.aimam.App;
import vn.gmorunsystem.aimam.utils.NetworkUtils;


public abstract class UploadRawDataApiRequest<T> extends BaseApiRequest<T> {

    private String imagePath;
    private String body;

    @Override
    protected void excecuteRequest() {
        super.excecuteRequest();
        baseTypeRequest = new BinaryRequest<>(getMethod(), createRequestUrl(), getErrorListener(), getResponseClass(), getRequestHeaders(), getListener(), imagePath, body);
        baseTypeRequest.setGsonRequestHeaderOnResult(this);
        baseTypeRequest.setRetryPolicy(getDefaultRetryPolicy());
        NetworkUtils.getInstance(App.getInstance()).addToRequestQueue(baseTypeRequest);
    }

    public void setRawFile(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setRawBody(String body) {
        this.body = body;
    }
}
