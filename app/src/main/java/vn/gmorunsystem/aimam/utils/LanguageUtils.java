package vn.gmorunsystem.aimam.utils;

import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

/**
 * Created by hungnl on 8/30/2017.
 */

public class LanguageUtils {


    public static final int LANG_EN = 1;
    public static final int LANG_VI = 0;

    public static final String LOCALE_EN = "en";
    public static final String LOCALE_VI = "vi";

    public static String getLanguageCode(int languageId) {
        switch (languageId) {
            case LANG_VI:
                return LOCALE_VI;
            case LANG_EN:
                return LOCALE_EN;
        }
        return LOCALE_EN;
    }

    public static void changeLocale(Resources res) {
        Locale myLocale = new Locale(LanguageUtils.getLanguageCode(SharedPrefUtils.getLanguageSetting()));
        Configuration conf = res.getConfiguration();
        conf.setLocale(myLocale);
        res.updateConfiguration(conf, null);
    }
}
