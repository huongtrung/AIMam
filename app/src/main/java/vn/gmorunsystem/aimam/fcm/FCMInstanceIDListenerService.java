package vn.gmorunsystem.aimam.fcm;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import vn.gmorunsystem.aimam.utils.DebugLog;
import vn.gmorunsystem.aimam.utils.SharedPrefUtils;

/**
 * Created by HuongTrung on 08/17/2017.
 */

public class FCMInstanceIDListenerService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        sendRegistrationToServer(refreshedToken);
        DebugLog.e("token :" + refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
        SharedPrefUtils.saveFireBaseToken(token);
        SharedPrefUtils.saveStatusSendDeviceInfo(false);
    }
}
