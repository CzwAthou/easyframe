package com.athou.frame.handler;

import android.content.DialogInterface;

import com.athou.frame.widget.dialog.AlertDialogSample;
import com.athou.frame.widget.dialog.LoadDialogSample;
import com.athou.frame.widget.dialog.ProgressDialogSample;
import com.athou.frame.widget.dialog.handler.ICustomDialog;
import com.athou.frame.widget.dialog.handler.ILoadDialog;
import com.athou.frame.widget.dialog.handler.IProgressDialog;

/**
 * Created by cai on 2016/9/25.
 */

public interface IDialogHandler {
    ICustomDialog showDialog(String title, String msg);

    ICustomDialog showDialog(String title, String msg, DialogInterface.OnClickListener ok);

    ICustomDialog showDialog(String title, String msg, DialogInterface.OnClickListener ok, DialogInterface.OnClickListener cancel);

    ICustomDialog showDialog(String title, String msg, String okStr, DialogInterface.OnClickListener ok, String cancelStr, DialogInterface.OnClickListener cancel);

    void hideDialog();

    ILoadDialog showLoadDialog();

    ILoadDialog showLoadDialog(String title, String msg);

    void hideLoadDialog();

    IProgressDialog showProgressDialog(long total, long progress);

    void hideProgressDialog();

    interface IDialogFactory {
        Class<? extends ICustomDialog> getICustomDialog();

        Class<? extends ILoadDialog> getILoadDialog();

        Class<? extends IProgressDialog> getIProgressDialog();
    }

    class DefaultDialogFactory implements IDialogFactory {
        @Override
        public Class<? extends ICustomDialog> getICustomDialog() {
            return AlertDialogSample.class;
        }

        @Override
        public Class<? extends ILoadDialog> getILoadDialog() {
            return LoadDialogSample.class;
        }

        @Override
        public Class<? extends IProgressDialog> getIProgressDialog() {
            return ProgressDialogSample.class;
        }
    }
}
