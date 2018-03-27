package vn.gmorunsystem.aimam.apis.volley.core;

import java.io.File;
import java.util.Map;

import vn.gmorunsystem.aimam.App;
import vn.gmorunsystem.aimam.utils.NetworkUtils;


public abstract class UploadBinaryApiRequest<T> extends BaseApiRequest<T> {

    private Map<String, File> requestFiles;

    @Override
    protected void excecuteRequest() {
        super.excecuteRequest();
        baseTypeRequest = new MultiPartRequest<>(getMethod(), createRequestUrl(), getErrorListener(), getResponseClass(), getRequestHeaders(), getListener(), handleRequestParams(), requestFiles);
        baseTypeRequest.setGsonRequestHeaderOnResult(this);
        baseTypeRequest.setRetryPolicy(getDefaultRetryPolicy());
        NetworkUtils.getInstance(App.getInstance()).addToRequestQueue(baseTypeRequest);
    }

    public void setRequestFiles(Map<String, File> requestFiles) {
        this.requestFiles = requestFiles;
    }
}
