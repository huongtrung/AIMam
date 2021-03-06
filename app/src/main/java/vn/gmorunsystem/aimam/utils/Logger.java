package vn.gmorunsystem.aimam.utils;

import android.util.Log;

/**
 * Created by Fuong Lee on 8/11/16.
 */
public class Logger {
    public static final boolean DEBUG = true; //BuildConfig.DEBUG

    public static void d(String tag, String message) {
        if (DEBUG) {
            Log.d(tag, message);
        }
    }

    public static void e(String tag, String message) {
        if (DEBUG) {
            Log.e(tag, message);
        }
    }

    public static void w(String tag, String message) {
        if (DEBUG) {
            Log.w(tag, message);
        }
    }

    public static void i(String tag, String message) {
        if (DEBUG) {
            Log.i(tag, message);
        }
    }

    public static void v(String tag, String message) {
        if (DEBUG) {
            Log.v(tag, message);
        }
    }
}
