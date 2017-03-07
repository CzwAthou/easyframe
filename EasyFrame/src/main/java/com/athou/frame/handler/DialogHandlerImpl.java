/*
 * Copyright (c) 2017  athou（cai353974361@163.com）.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.athou.frame.handler;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.athou.frame.constants.Constants;
import com.athou.frame.widget.dialog.handler.ICustomDialog;
import com.athou.frame.widget.dialog.handler.IDialog;
import com.athou.frame.widget.dialog.handler.ILoadDialog;
import com.athou.frame.widget.dialog.handler.IProgressDialog;

import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by athou on 2017/3/1.
 */

public class DialogHandlerImpl implements IDialogHandler {
    private ICustomDialog customDialog;
    private ILoadDialog loadDialog;
    private IProgressDialog progressDialog;

    private WeakReference<Activity> mActivity;

    private static IDialogFactory defaultDialogFactory = new DefaultDialogFactory();

    private Handler dialogHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constants.SHOW_CUSTOM_DIALOG:
                    showDialog(msg.getData().getString("Title"), msg.getData().getString("Msg"));
                    break;
                case Constants.HIDE_CUSTOM_DIALOG:
                    hideDialog();
                    break;
                case Constants.SHOW_LOAD_DIALOG:
                    showLoadDialog();
                    break;
                case Constants.HIDE_LOAD_DIALOG:
                    hideLoadDialog();
                    break;
                case Constants.SHOW_PROGRESS:
                    showProgressDialog(msg.getData().getLong("Total", 0), msg.getData().getLong("Progress", 0));
                    break;
                case Constants.HIDE_PROGRESS:
                    hideProgressDialog();
                    break;
            }
        }
    };

    public DialogHandlerImpl(Activity activity) {
        mActivity = new WeakReference<Activity>(activity);
    }

    public static void setDialogFactory(IDialogFactory factory) {
        defaultDialogFactory = factory;
    }

    private <T> T createDialogInstance(Class<T> cls) {
        if (checkActivity()) {
            try {
                Constructor<T> cConstructor = cls.getConstructor(Context.class);
                IDialog<T> dialog = (IDialog) cConstructor.newInstance(mActivity.get());
                return (T) dialog;
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private boolean checkActivity() {
        return mActivity.get() != null;
    }

    public void showDialogInThread(String title, String msgStr) {
        Message msg = dialogHandler.obtainMessage(Constants.SHOW_CUSTOM_DIALOG);
        Bundle bundle = new Bundle();
        bundle.putString("Title", title);
        bundle.putString("Msg", msgStr);
        msg.setData(bundle);
        dialogHandler.sendMessage(msg);
    }

    public void hideDialogInThread() {
        Message msg = dialogHandler.obtainMessage(Constants.HIDE_CUSTOM_DIALOG);
        dialogHandler.sendMessage(msg);
    }

    @Override
    public ICustomDialog showDialog(String title, String msg) {
        hideDialog();

        customDialog = createDialogInstance(defaultDialogFactory.getICustomDialog());
        if (customDialog == null) {
            return null;
        }
        customDialog.setTitle(title);
        customDialog.setMessage(msg);
        customDialog.show();
        return customDialog;
    }

    @Override
    public ICustomDialog showDialog(String title, String msg, DialogInterface.OnClickListener ok) {
        hideDialog();

        customDialog = createDialogInstance(defaultDialogFactory.getICustomDialog());
        if (customDialog == null) {
            return null;
        }
        customDialog.setTitle(title);
        customDialog.setMessage(msg);
        customDialog.setPositiveButton("确定", ok);
        customDialog.show();
        return customDialog;
    }

    @Override
    public ICustomDialog showDialog(String title, String msg, DialogInterface.OnClickListener ok, DialogInterface.OnClickListener cancel) {
        hideDialog();

        customDialog = createDialogInstance(defaultDialogFactory.getICustomDialog());
        if (customDialog == null) {
            return null;
        }
        customDialog.setTitle(title);
        customDialog.setMessage(msg);
        customDialog.setPositiveButton("确定", ok);
        customDialog.setNegativeButton("取消", cancel);
        customDialog.show();
        return customDialog;
    }

    public ICustomDialog showDialog(String title, String msg, String okStr, DialogInterface.OnClickListener ok, String cancelStr, DialogInterface.OnClickListener cancel) {
        hideDialog();

        customDialog = createDialogInstance(defaultDialogFactory.getICustomDialog());
        if (customDialog == null) {
            return null;
        }
        customDialog.setTitle(title);
        customDialog.setMessage(msg);
        customDialog.setPositiveButton(okStr, ok);
        customDialog.setNegativeButton(cancelStr, cancel);
        customDialog.show();
        return customDialog;
    }

    @Override
    public void hideDialog() {
        if (customDialog != null && customDialog.isShowing()) {
            customDialog.dismiss();
        }
        customDialog = null;
    }

    public void showLoadDialogInThread() {
        Message msg = dialogHandler.obtainMessage(Constants.SHOW_LOAD_DIALOG);
        dialogHandler.sendMessage(msg);
    }

    public void hideLoadDialogInThread() {
        Message msg = dialogHandler.obtainMessage(Constants.HIDE_LOAD_DIALOG);
        dialogHandler.sendMessage(msg);
    }

    @Override
    public ILoadDialog showLoadDialog() {
        hideLoadDialog();
        loadDialog = createDialogInstance(defaultDialogFactory.getILoadDialog());
        if (loadDialog == null) {
            return null;
        }
        loadDialog.show();
        return loadDialog;
    }

    @Override
    public ILoadDialog showLoadDialog(String title, String msg) {
        hideLoadDialog();
        loadDialog = createDialogInstance(defaultDialogFactory.getILoadDialog());
        if (loadDialog == null) {
            return null;
        }
        loadDialog.setTitle(title);
        loadDialog.setMessage(msg);
        loadDialog.show();
        return loadDialog;
    }

    @Override
    public void hideLoadDialog() {
        if (loadDialog != null && loadDialog.isShowing()) {
            loadDialog.dismiss();
        }
        loadDialog = null;
    }

    public void showProgressDialogInThread(long total, long progress) {
        Message msg = dialogHandler.obtainMessage(Constants.SHOW_PROGRESS);
        Bundle bundle = new Bundle();
        bundle.putLong("Total", total);
        bundle.putLong("Progress", progress);
        msg.setData(bundle);
        dialogHandler.sendMessage(msg);
    }

    public void hideProgressDialogInThread() {
        Message msg = dialogHandler.obtainMessage(Constants.HIDE_PROGRESS);
        dialogHandler.sendMessage(msg);
    }

    @Override
    public IProgressDialog showProgressDialog(long total, long progress) {
        if (progressDialog == null) {
            progressDialog = createDialogInstance(defaultDialogFactory.getIProgressDialog());
            if (progressDialog == null) {
                return null;
            }
            progressDialog.setIndeterminate(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        }
        progressDialog.setProgress(total, progress);
        progressDialog.show();
        return progressDialog;
    }

    @Override
    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.setProgress(0, 0);
            progressDialog.dismiss();
        }
    }
}
