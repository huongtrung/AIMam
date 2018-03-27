package vn.gmorunsystem.aimam.utils;

import android.content.Context;

import vn.gmorunsystem.aimam.R;
import vn.gmorunsystem.aimam.constants.APIConstant;
import vn.gmorunsystem.aimam.constants.APPConstant;


/**
 * Created by HuongTrung on 09/26/2017.
 */

public class CheckErrorCodeUtil {

    public static void showDialogError(final Context context, int errorCode) {
        switch (errorCode) {
            case APIConstant.ERROR_CODE_999:
                DialogUtil.showDialog(context, context.getString(R.string.error_time_out));
                break;
            case APIConstant.ERROR_CODE_998_JSON_SYNTAX:
                DialogUtil.showDialog(context, context.getString(R.string.error_not_json));
                break;
            case APIConstant.ERROR_CODE_500:
                DialogUtil.showDialog(context, context.getString(R.string.message_error_code_500));
                break;
            case APIConstant.ERROR_CODE_400:
                DialogUtil.showDialog(context, context.getString(R.string.message_error_code_404));
                break;
            case APIConstant.ERROR_CODE_401:
                DialogUtil.showDialog(context, context.getString(R.string.message_error_code_401));
                break;
            case APIConstant.ERROR_CODE_404:
                DialogUtil.showDialog(context, context.getString(R.string.message_error_code_400));
                break;
            case APIConstant.ERROR_CODE_409:
                DialogUtil.showDialog(context, context.getString(R.string.message_error_code_409));
                break;
            default:
                DialogUtil.showDialog(context, context.getString(R.string.error_server));
                break;

        }
    }

    public static void showDialogSpecialError(Context context, int errorCode, String message, String isFromApi) {
        String typeError = "";
        String valueError = "";
        switch (errorCode) {
            case APIConstant.ERROR_CODE_999:
                DialogUtil.showDialog(context, context.getString(R.string.error_time_out));
                break;
            case APIConstant.ERROR_CODE_500:
                DialogUtil.showDialog(context, context.getString(R.string.message_error_code_500));
                break;
            case APIConstant.ERROR_CODE_400:
                if (message.contains(APIConstant.TYPE_ERROR) && message.contains(APIConstant.ERROR_VALUE)) {
                    // trim message contain type and value of message error response
                    typeError = AppUtils.trimMessage(message, APIConstant.TYPE_ERROR);
                    valueError = AppUtils.trimMessage(message, APIConstant.ERROR_VALUE);
                }
                if (typeError != null && valueError != null) {
                    // switch case from api special error response
                    switch (isFromApi) {
                        case APPConstant.API_CHECK_IN:
                            // api check in have 3 type error : checkin_radius ,stamp_number_limit, time_between_checkins
                            switch (typeError) {
                                case APPConstant.TYPE_RADIUS:
                                    DialogUtil.showDialog(context, String.format(context.getString(R.string.message_not_enough_distance), valueError));
                                    break;
                                case APPConstant.TYPE_NUMBER_LIMIT:
                                    DialogUtil.showDialog(context, String.format(context.getString(R.string.message_not_enough_stamp), valueError));
                                    break;
                                case APPConstant.TYPE_TIME:
                                    DialogUtil.showDialog(context, String.format(context.getString(R.string.message_not_enough_time), valueError));
                                    break;
                            }
                            break;
                        case APPConstant.API_REGISTER:
                            // check error api register have errors = email
                            if (valueError.equalsIgnoreCase(APPConstant.ERROR_EMAIL)) {
                                switch (typeError) {
                                    // api register have 2 type error : taken ,not_live
                                    case APPConstant.TYPE_TAKEN:
                                        DialogUtil.showDialog(context, context.getString(R.string.message_email_taken));
                                        break;
                                    case APPConstant.TYPE_NOT_LIVE:
                                        DialogUtil.showDialog(context, context.getString(R.string.message_email_not_alive));
                                        break;
                                }
                            } else {
                                DialogUtil.showDialog(context, context.getString(R.string.error_server));
                            }
                            break;
                        case APPConstant.API_LOGIN:
                            DialogUtil.showDialog(context, context.getString(R.string.message_email_pass_wrong));
                            break;
                        case APPConstant.API_GET_GIFT:
                            DialogUtil.showDialog(context, context.getString(R.string.message_error_get_gift));
                            break;
                        case APPConstant.API_LOGIN_SOCIAL:
                            DialogUtil.showDialog(context, context.getString(R.string.message_email_taken));
                            break;
                    }
                }
                break;
            case APIConstant.ERROR_CODE_401:
                DialogUtil.showDialog(context, context.getString(R.string.message_error_code_401));
                break;
            case APIConstant.ERROR_CODE_404:
                if (isFromApi.equalsIgnoreCase(APPConstant.API_LOGIN)) {
                    DialogUtil.showDialog(context, context.getString(R.string.message_user_not_found));
                } else {
                    DialogUtil.showDialog(context, context.getString(R.string.message_error_code_404));
                }
                break;
            case APIConstant.ERROR_CODE_998_JSON_SYNTAX:
                DialogUtil.showDialog(context, context.getString(R.string.error_not_json));
                break;
            default:
                DialogUtil.showDialog(context, context.getString(R.string.error_server));
                break;
        }
    }
}
