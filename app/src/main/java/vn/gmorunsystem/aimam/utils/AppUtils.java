package vn.gmorunsystem.aimam.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Veteran Commander on 6/5/2017.
 */

public class AppUtils {

    private static final Gson gson = new Gson();

    public static boolean isJSONValid(String jsonInString) {
        try {
            gson.fromJson(jsonInString, Object.class);
            return true;
        } catch (com.google.gson.JsonSyntaxException ex) {
            return false;
        }
    }

    public static String trimMessage(String json, String key) {
        String trimmedString = null;

        try {
            JSONObject obj = new JSONObject(json);
            if (obj.has(key)) {
                trimmedString = obj.getString(key);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return trimmedString;
    }

    public static File creatFilefromBitmap(Bitmap bitmap) throws IOException {
        File imageAvatar;
        File imageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/AiMăm");
        imageDir.mkdir();
        imageAvatar = new File(imageDir, "avatar.jpg");
        DebugLog.i("Duong dan" + imageDir);
        OutputStream fOut = new FileOutputStream(imageAvatar);
        Bitmap getBitmap = bitmap;
        getBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        fOut.flush();
        fOut.close();
        return imageAvatar;
    }

    public static int getDeviceScreenHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;

    }

    public static int getDeviceScreenWidth(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;

    }

    public static String makeDir(String directory) {
        File storageDir = new File(directory);
        if (!storageDir.exists()) {
            storageDir.mkdir();
        }
        String appDir = storageDir.getAbsolutePath();
        return appDir;
    }

    public static void loadAdBanner(AdView adBanner, final Context context) {
        adBanner.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                DebugLog.e("Ad is closed!");
            }

            @Override
            public void onAdFailedToLoad(int i) {
                DebugLog.e("Ad failed to load! error code: " + i);
            }

            @Override
            public void onAdLeftApplication() {
                DebugLog.e("Ad left");
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }
        });

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                // Check the LogCat to get your test device ID
                .addTestDevice("D5DA4822065F05FFADE2E7FE5275EEE8")
                .build();
        adBanner.loadAd(adRequest);
    }

    public static void goToMainOrAdScreen(Activity activity) {
        String currentDate = getCurrentDate();
        if (!SharedPrefUtils.getSavedCurrentDate().equals(currentDate)) {
            SharedPrefUtils.saveCurrentDate(currentDate);
            IntentUtil.goToAdMob(activity);
        } else {
            IntentUtil.gotoMain(activity);
        }
    }

    public static String getCurrentDate() {
        String currentDate;
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        currentDate = df.format(c.getTime());
        return currentDate;
    }

}
