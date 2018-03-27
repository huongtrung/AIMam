package vn.gmorunsystem.aimam.utils;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Pattern;

import vn.gmorunsystem.aimam.constants.APPConstant;

/**
 * Created by Envy 15T on 6/5/2015.
 */
public class StringUtil {

    /**
     * Upper case first letter in string
     *
     * @param str
     * @return
     */
    public static final String data = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String PHONE_NUMBER_REGREX = "[0]\\d{9,10}+";

    public static String UppercaseFirstLetters(String str) {
        boolean prevWasWhiteSp = true;
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (Character.isLetter(chars[i])) {
                if (prevWasWhiteSp) {
                    chars[i] = Character.toUpperCase(chars[i]);
                }
                prevWasWhiteSp = false;
            } else {
                prevWasWhiteSp = Character.isWhitespace(chars[i]);
            }
        }
        return new String(chars);
    }

    public static String formatDate(int year, int monthOfYear, int dayOfMonth) {
        return year + "/" + monthOfYear + "/" + dayOfMonth;
    }

    public static String formatDate(Calendar calendar) {
        return formatDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
    }

    public static boolean checkStringValid(String data) {
        if (data != null && !data.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean checkStringNull(String data) {
        if (data != null) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean checkZero(String data) {
        if (checkStringValid(data) && !data.equals("0"))
            return true;
        else
            return false;
    }

    public static void displayText(String text, TextView textView) {
        if (textView == null)
            return;
        if (text != null && !text.isEmpty() && !text.equals("null")) {
            textView.setText(text);
        } else {
            textView.setText("");
        }

    }

    public static void displayTextNull(String text, TextView textView) {
        if (textView == null)
            textView.setVisibility(View.GONE);
        if (text != null && !text.isEmpty() && !text.equals("null")) {
            textView.setText(text);
        } else {
            textView.setText("");
        }

    }

    public static void displayTextResource(int textResId, TextView textView) {
        if (textView == null)
            return;
        textView.setText(textView.getContext().getResources().getString(textResId));
    }

    public static void setTextColor(int textColorId, TextView textView) {
        if (textView == null)
            return;
        textView.setTextColor(textView.getContext().getResources().getColor(textColorId));
    }

    public static void setImageResource(int imageRes, ImageView imageView) {
        if (imageView == null)
            return;
        imageView.setImageResource(imageRes);
    }

    public static void setGravity(int gravity, TextView textView) {
        if (textView == null)
            return;
        textView.setGravity(gravity);
    }

    public static void displayText(String text, TextView textView, String prefix) {
        if (textView == null)
            return;
        if (text != null && !text.isEmpty() && !text.equals("null")) {
            textView.setText(new StringBuilder().append(text).append(prefix));
        } else {
            textView.setText(new StringBuilder().append("0").append(prefix));
        }

    }

    public static void displayText(Float text, TextView textView) {
        if (text != null) {
            String.format(Locale.getDefault(), "%.0f", text);
        } else {
            textView.setText("");
        }
    }

    public static void displayText(int text, TextView textView) {
        if (text > 0) {
            textView.setText(String.valueOf(text));
        } else {
            textView.setText("");
        }
    }

    public static void displayTextEqualZero(int text, TextView textView) {
        if (text > 0) {
            textView.setText(String.valueOf(text));
        } else {
            textView.setText("0");
        }
    }

    /**
     * Check valid email
     *
     * @param target
     * @return true if valid email, false if invalid email
     */
    public static boolean isValidEmail(CharSequence target) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static DecimalFormat setDecimalFormat() {
        Locale locale = new Locale("en", "UK");
        String pattern = "#,###";
        DecimalFormat decimalFormat = (DecimalFormat)
                NumberFormat.getNumberInstance(locale);
        decimalFormat.applyPattern(pattern);
        return decimalFormat;
    }

    public static void fillPrefix(TextView tv, float value, String prefix) {
        if (value == 0) {
            tv.setText(new StringBuilder(prefix).append("0.00"));
        } else if (value > 0) {
            if (!convertTextFloattoInt(String.valueOf(value))) {
                tv.setText(new StringBuilder(prefix).append(setDecimalFormat().format(value)));
            } else {
                tv.setText(new StringBuilder(prefix).append(setDecimalFormat().format(value)));
            }
        } else {
            tv.setText("");
        }
    }

    public static void fillSuffixes(TextView tv, float value, String suffixes) {
        if (value == 0) {
            tv.setText(new StringBuilder("0.00").append(suffixes));
        } else if (value > 0) {
            if (!convertTextFloattoInt(String.valueOf(value))) {
                tv.setText(new StringBuilder(setDecimalFormat().format(value)).append(suffixes));
            } else {
                tv.setText(new StringBuilder(setDecimalFormat().format(value)).append(suffixes));
            }
        } else {
            tv.setText("");
        }
    }

    /**
     * Ellipsize string with maxCharacter
     *
     * @param input
     * @param maxCharacters
     * @return
     */
    public static String ellipsize(String input, int maxCharacters) {
        if (maxCharacters < 3) {
            throw new IllegalArgumentException("maxCharacters must be at least 3 because the ellipsis already take up 3 characters");
        }
        if (input == null || input.length() < maxCharacters) {
            return input;
        }
        return input.substring(0, maxCharacters - 3) + "...";
    }

    public static boolean checkTypeFloat(String value) {
        if (value.contains(".")) {
            return true;
        }
        return false;
    }

    public static boolean convertTextFloattoInt(String value) {
        String result5[] = value.split("[.]");
        int b = Integer.parseInt(result5[1]);
        if (b > 0) {
            return true;
        }
        return false;
    }

    public static boolean isEmpty(String input) {
        if (input == null || input.length() == 0) {
            return true;
        }
        return false;
    }

    public static String addString(String text) {
        return String.format("     %s     %s     %s", text, text, text);
    }

    public static boolean isPhoneNumberValid(String phone) {
        String phonePattern = "^0\\d{1,4}[-]{0,1}\\d{1,4}[-]{0,1}\\d{4}$";
        Pattern pattern = Pattern.compile(phonePattern);
        return pattern.matcher(phone).matches();
    }

    public static String randomString() {
        int len = 5;
        Random random = new Random();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(data.charAt(random.nextInt(data.length())));
        }
        return sb.toString();
    }

    public static String formatDatetime(String formatIn) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss Z");

        try {
            Date date = sdf.parse(formatIn);
            SimpleDateFormat destDf = new SimpleDateFormat("dd/MM/yyyy");
            formatIn = destDf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatIn;
    }

    public static String formatDateCoupon(String format, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss+ss:ss");
        try {
            Date date = sdf.parse(format);
            SimpleDateFormat destDf = new SimpleDateFormat(dateFormat, Locale.US);
            format = destDf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return format;
    }

    public static String convertDistanceToString(float distance) {
        if (distance == APPConstant.NOT_STORE_LOCATION_OR_USER_LOCATION)
            return "--";
        if (distance < 1000) {
            return (int) distance + "m";
        } else if (distance < 10000)
            return roundOneDecimals(distance / 1000) + "km";
        else if (distance < 100000) {
            return roundOneDecimals(distance / 1000) + "km";
        } else
            return "100km+";
    }

    private static String roundOneDecimals(Float f) {
        DecimalFormat oneDForm = new DecimalFormat("##.#");
        return oneDForm.format(f);
    }

    public static String formatNumber(double number) {
        Locale locale = new Locale("vi", "VN");
        Currency currency = Currency.getInstance("VND");

        DecimalFormatSymbols df = DecimalFormatSymbols.getInstance(locale);
        df.setCurrency(currency);
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        numberFormat.setCurrency(currency);
        String money = numberFormat.format(number);

        return money.replace("₫", "VNĐ");
    }

    public static String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(calendar.getTime());
        return formattedDate;
    }

    //String error from server response not volley error
    public static String getStringErrorFromJSONArray(JSONArray jsonArray, String prefix) {
        String value = "";
        for (int i = 0; i < jsonArray.length(); i++)
            try {
                value = value + prefix + " " + jsonArray.getString(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return value;
    }
}
