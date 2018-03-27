package vn.gmorunsystem.aimam;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import vn.gmorunsystem.aimam.utils.NetworkUtils;
import vn.gmorunsystem.aimam.utils.SharedPrefUtils;

public class App extends MultiDexApplication {

    private static App instance;
    private static SharedPrefUtils sharedPreferences;

    public App() {
        // TODO Auto-generated constructor stub
        instance = this;
    }

    public static App getInstance() {
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(App.this);
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        sharedPreferences = new SharedPrefUtils(getApplicationContext());
        NetworkUtils.getInstance(getApplicationContext());
    }

    public static SharedPrefUtils getSharedPreferences() {
        return sharedPreferences;
    }
}
