package vn.gmorunsystem.aimam.fcm;

import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import vn.gmorunsystem.aimam.App;
import vn.gmorunsystem.aimam.R;
import vn.gmorunsystem.aimam.constants.APPConstant;
import vn.gmorunsystem.aimam.ui.activities.SplashActivity;
import vn.gmorunsystem.aimam.utils.DebugLog;
import vn.gmorunsystem.aimam.utils.SharedPrefUtils;

/**
 * Created by HuongTrung on 08/17/2017.
 */

public class FCMListenerService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        DebugLog.e("from" + remoteMessage.getFrom());
        DebugLog.e("body" + remoteMessage.getNotification().getBody());
        if (SharedPrefUtils.getStatusNotify() == APPConstant.FRAG_ON) {
            sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }
    }

    private void sendNotification(String title, String message) {
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
        if (message != null)
            intent.putExtra(APPConstant.NOTIFICATION, message);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 96 /* Request code */, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(App.getInstance())
                .setSmallIcon(getNotificationIcon())
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setColor(getResources().getColor(R.color.color_bg_gray))
                .setContentIntent(pendingIntent)
                .setSound(alarmSound);
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getApplicationContext());
        managerCompat.notify(0, notificationBuilder.build());
    }

    private int getNotificationIcon() {
        return R.drawable.ic_logo;
    }
}
