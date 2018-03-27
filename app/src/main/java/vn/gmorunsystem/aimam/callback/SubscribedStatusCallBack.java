package vn.gmorunsystem.aimam.callback;

/**
 * Created by HuongTrung on 09/18/2017.
 */

public class SubscribedStatusCallBack {
    private boolean checkSubscribed;

    public SubscribedStatusCallBack(boolean checkSubscribed) {
        this.checkSubscribed = checkSubscribed;
    }

    public boolean isSubscribedStatus() {
        return checkSubscribed;
    }
}
