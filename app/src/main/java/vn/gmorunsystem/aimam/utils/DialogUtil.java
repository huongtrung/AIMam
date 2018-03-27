package vn.gmorunsystem.aimam.utils;

import android.support.v7.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import vn.gmorunsystem.aimam.R;


public class DialogUtil {
    public static AlertDialog showDialog(final Context context, String message) {
        if (context == null) {
            return null;
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);

        alertDialogBuilder.setMessage(message);

        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        alertDialogBuilder.setCancelable(false);
        AlertDialog alertDialog = alertDialogBuilder.create();
        try {
            alertDialog.show();
            return alertDialog;
        } catch (Exception e) {
            return null;
        }

    }

    public static AlertDialog createDialog(final Context context, String title, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context, R.style.MyAlertDialogStyle);

        if (title != null && !title.isEmpty()) {
            alertDialogBuilder.setTitle(title);
        }

        alertDialogBuilder.setMessage(message);

        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        alertDialogBuilder.setCancelable(false);
        AlertDialog alertDialog = alertDialogBuilder.create();
        try {
            return alertDialog;
        } catch (Exception e) {
            return null;
        }
    }

    public static AlertDialog showDialogComfirm(Context context, String message, String okText, DialogInterface.OnClickListener okOnClickListener) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context, R.style.MyAlertDialogStyle);

        alertDialogBuilder.setMessage(message);

        alertDialogBuilder.setPositiveButton(okText, okOnClickListener);
        alertDialogBuilder.setCancelable(false);
        AlertDialog alertDialog = alertDialogBuilder.create();
        try {
            return alertDialog;
        } catch (Exception e) {
            return null;
        }
    }

    public static AlertDialog showDialogFull(Context context, String title, String message, String okText, String cancelText, DialogInterface.OnClickListener okOnClickListener, DialogInterface.OnClickListener cancelOnClickListener) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context, R.style.MyAlertDialogStyle);
        if (title != null && !title.isEmpty()) {
            alertDialogBuilder.setTitle(title);
        }

        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setPositiveButton(okText, okOnClickListener);
        alertDialogBuilder.setNegativeButton(cancelText, cancelOnClickListener);

        AlertDialog alertDialog = alertDialogBuilder.create();
        try {
            alertDialog.show();
            return alertDialog;
        } catch (Exception e) {
            return null;
        }
    }

    public static AlertDialog showDialogAsk(Context context, String title, String message, String okText, String cancelText, DialogInterface.OnClickListener okOnClickListener, DialogInterface.OnClickListener cancelOnClickListener) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context, R.style.MyAlertDialogStyle);
        if (title != null && !title.isEmpty()) {
            alertDialogBuilder.setTitle(title);
        }

        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton(okText, okOnClickListener);
        alertDialogBuilder.setNegativeButton(cancelText, cancelOnClickListener);

        AlertDialog alertDialog = alertDialogBuilder.create();
        try {
            alertDialog.show();
            return alertDialog;
        } catch (Exception e) {
            return null;
        }
    }

    public static AlertDialog showDialogConfirmExit(Context context, String message, String okText, String cancelText, DialogInterface.OnClickListener okOnClickListener) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setNegativeButton(okText, okOnClickListener);
        alertDialogBuilder.setPositiveButton(cancelText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        final AlertDialog alertDialog = alertDialogBuilder.create();
        try {
            alertDialog.show();
            return alertDialog;
        } catch (Exception e) {
            return null;
        }
    }

    public static AlertDialog showDialogNext(Context context, String message, String okText, DialogInterface.OnClickListener okOnClickListener) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context, R.style.MyAlertDialogStyle);

        alertDialogBuilder.setMessage(message);

        alertDialogBuilder.setPositiveButton(okText, okOnClickListener);
        alertDialogBuilder.setCancelable(false);
        AlertDialog alertDialog = alertDialogBuilder.create();
        try {
            alertDialog.show();
            return alertDialog;
        } catch (Exception e) {
            return null;
        }
    }

    public static AlertDialog showOkBtnDialog(final Context context, String title, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context, R.style.MyAlertDialogStyle);

        if (title != null && !title.isEmpty()) {
            alertDialogBuilder.setTitle(title);
        }

        alertDialogBuilder.setMessage(message);

        alertDialogBuilder.setPositiveButton(R.string.title_yes,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        alertDialogBuilder.setCancelable(false);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        return alertDialog;
    }

    public static AlertDialog createOneBtnWithHandleDialog(final Context context, String title, String message, String okBtnText, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context, R.style.MyAlertDialogStyle);

        if (title != null && !title.isEmpty()) {
            alertDialogBuilder.setTitle(title);
        }

        alertDialogBuilder.setMessage(message);

        alertDialogBuilder.setPositiveButton(okBtnText,
                onClickListener);

        alertDialogBuilder.setCancelable(false);
        return alertDialogBuilder.create();
    }


    public static AlertDialog showApiErrorDialogDialog(final Context context) {
        return showOkBtnDialog(context, null, context.getString(R.string.title_yes));
    }

    public static AlertDialog createApiErrorDialog(final Context context, String message, DialogInterface.OnClickListener onClickListener) {
        return createOneBtnWithHandleDialog(context, null, message, context.getString(R.string.title_yes), onClickListener);
    }
}
