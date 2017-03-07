package com.athou.frame.util;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Toast统一管理类
 *
 * @author athou
 */
public class T {

    /**
     * 默认显示的Toast
     *
     * @param context
     * @param msgStr
     */
    public static Toast show(Context context, CharSequence msgStr) {
        Toast toast = Toast.makeText(context, msgStr, Toast.LENGTH_SHORT);
        toast.show();
        return toast;
    }

    /**
     * 默认显示的Toast
     *
     * @param context
     * @param msgResId
     */
    public static Toast show(Context context, int msgResId) {
        Toast toast = Toast.makeText(context, msgResId, Toast.LENGTH_SHORT);
        toast.show();
        return toast;
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static Toast showLong(Context context, CharSequence message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.show();
        return toast;
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static Toast showLong(Context context, int message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.show();
        return toast;
    }

    /**
     * 自定义显示Toast view
     *
     * @param context
     * @param view
     */
    public static Toast showView(Context context, View view) {
        Toast viewToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        viewToast.setView(view);
        viewToast.show();
        return viewToast;
    }

    /**
     * 自定义显示Toast icon
     *
     * @param context
     * @param icon
     */
    public static Toast showIcon(Context context, int icon) {
        return showIcon(context, null, icon);
    }

    /**
     * 自定义显示Toast icon
     *
     * @param context
     * @param msg
     * @param icon
     */
    public static Toast showIcon(Context context, String msg, int icon) {
        Toast iconToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        iconToast.setGravity(Gravity.CENTER, 0, 0);
        LinearLayout toastView = (LinearLayout) iconToast.getView();
        TextView tv = (TextView) toastView.getChildAt(0);
        tv.setText(msg);
        tv.setCompoundDrawables(null, context.getResources().getDrawable(icon), null, null);
        tv.setGravity(Gravity.CENTER);
        iconToast.show();
        return iconToast;
    }

    /**
     * showSnacke -- LENGTH_SHORT
     */
    public static Snackbar showSnacke(View view, String msg) {
        Snackbar snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_SHORT);
        snackbar.show();
        return snackbar;
    }

    /**
     * showSnackeLong -- LENGTH_LONG
     */
    public static Snackbar showSnackeLong(View view, String msg) {
        Snackbar snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG);
        snackbar.show();
        return snackbar;
    }

    /**
     * showSnacke -- with action
     */
    public static Snackbar showSnacke(View view, String msg, String action, View.OnClickListener actionClick) {
        Snackbar snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
                .setAction(action, actionClick);
        snackbar.show();
        return snackbar;
    }
}
