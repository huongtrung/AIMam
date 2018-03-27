package vn.gmorunsystem.aimam.callback;

/**
 * Created by HuongTrung on 30/11/2016.
 */

public class SuccessCallBack {

    private boolean mCheckSuccessApi;

    public SuccessCallBack(boolean checkSuccessApi) {
        this.mCheckSuccessApi = checkSuccessApi;
    }

    public boolean isCheckSuccessApi() {
        return mCheckSuccessApi;
    }
}
