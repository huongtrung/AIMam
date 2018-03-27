package vn.gmorunsystem.aimam.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;

import java.io.File;

import vn.gmorunsystem.aimam.ui.activities.AdmobActivity;
import vn.gmorunsystem.aimam.ui.activities.LoginActivity;
import vn.gmorunsystem.aimam.ui.activities.MainActivity;
import vn.gmorunsystem.aimam.ui.activities.TutorialActivity;

/**
 * Created by Envy 15T on 10/6/2015.
 */
public class IntentUtil {

    public static void playYoutube(final Activity activity, String id) {
        DebugLog.e("playYoutube: " + id);
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
            activity.startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + id));
            activity.startActivity(intent);
        }
    }

    public static void openWebPage(final Activity activity, String url) {
        DebugLog.e("openWebPage: " + url);
        Uri webPage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webPage);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
        }
    }

    public static void openPhoneCall(final Activity activity, String tel) {
        try {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + tel));
            activity.startActivity(callIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void openPhoneSms(final Activity activity, String tel) {
        try {
            Intent smsIntent = new Intent(Intent.ACTION_VIEW);
            smsIntent.setType("vnd.android-dir/mms-sms");
            smsIntent.putExtra("address", tel);
            activity.startActivity(smsIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void openSendMail(final Activity activity, String email) {
        try {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:" + email));
            activity.startActivity(emailIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void openMap(final Activity activity, Uri mapUri) {
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(mapIntent);
        }
    }

    public static void openPdf(final Activity activity, String filePath) {
        File file = new File(filePath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        activity.startActivity(intent);
    }

    public static void openInstallApp(final Activity activity, String packageId) {
        try {
            Intent installIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageId));
            activity.startActivity(installIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Intent installIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageId));
            activity.startActivity(installIntent);
        }
    }

    public static void gotoMain(final Activity activity) {
        Intent intent = new Intent(activity.getApplicationContext(), MainActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    public static void gotoLogin(final Activity activity) {
        Intent intent = new Intent(activity.getApplicationContext(), LoginActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    public static void goToAdMob(final Activity activity) {
        Intent intent = new Intent(activity.getApplicationContext(), AdmobActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    public static void gotoTutorial(final Activity activity) {
        Intent intent = new Intent(activity.getApplicationContext(), TutorialActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }
}
