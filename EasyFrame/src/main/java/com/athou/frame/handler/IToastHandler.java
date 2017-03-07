package com.athou.frame.handler;

import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.athou.frame.net.NetErrorBean;

/**
 * Created by cai on 2016/9/25.
 */

public interface IToastHandler {
    Toast showToast(int resId);

    Toast showToast(NetErrorBean msg);

    Toast showToast(String msg);

    Toast showToastLong(String msg);

    Snackbar showSnacke(String msg);

    Snackbar showSnackeLong(String msg);

    Snackbar showSnacke(String msg, String action, View.OnClickListener onClickListener);
}
