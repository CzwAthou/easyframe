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
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.athou.frame.constants.Constants;
import com.athou.frame.net.NetErrorBean;
import com.athou.frame.util.T;

import java.lang.ref.WeakReference;

/**
 * Created by athou on 2017/3/1.
 */

public class ToastHandlerImpl implements IToastHandler {

    private WeakReference<Activity> mActivity;
    private WeakReference<View> mRootView;

    public ToastHandlerImpl(Activity activity, View rootView) {
        this.mActivity = new WeakReference<Activity>(activity);
        this.mRootView = new WeakReference<View>(rootView);
    }

    protected Handler toastHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constants.SHOW_TOAST:
                    showToast(msg.getData().getString("Msg"));
                    break;
                case Constants.SHOW_SNACK:
                    showSnacke(msg.getData().getString("Msg"));
                    break;
            }
        }
    };

    private boolean checkActivity() {
        return mActivity.get() != null;
    }

    private boolean checkRootView() {
        return mRootView.get() != null;
    }

    /**
     * 在线程中提示文本信息.
     */
    public void showToastInThread(String msgStr) {
        Message msg = toastHandler.obtainMessage(Constants.SHOW_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString("Msg", msgStr);
        msg.setData(bundle);
        toastHandler.sendMessage(msg);
    }

    /**
     * 在线程中提示文本信息.
     */
    public void showSnackInThread(String msgStr) {
        Message msg = toastHandler.obtainMessage(Constants.SHOW_SNACK);
        Bundle bundle = new Bundle();
        bundle.putString("Msg", msgStr);
        msg.setData(bundle);
        toastHandler.sendMessage(msg);
    }

    @Override
    public Toast showToast(int resId) {
        return showToast(mActivity.get().getResources().getString(resId));
    }

    @Override
    public Toast showToast(NetErrorBean msg) {
        if (msg != null && !msg.isSucceed()) {
            return showToast(msg.getErrMessge());
        }
        return null;
    }

    @Override
    public Toast showToast(String msg) {
        if (checkActivity()) {
            return T.show(mActivity.get(), msg);
        }
        return null;
    }

    @Override
    public Toast showToastLong(String msg) {
        if (checkActivity()) {
            return T.showLong(mActivity.get(), msg);
        }
        return null;
    }

    @Override
    public Snackbar showSnacke(String msg) {
        if (checkRootView()) {
            return T.showSnacke(mRootView.get(), msg);
        }
        return null;
    }

    @Override
    public Snackbar showSnackeLong(String msg) {
        if (checkRootView()) {
            return T.showSnackeLong(mRootView.get(), msg);
        }
        return null;
    }

    @Override
    public Snackbar showSnacke(String msg, String action, View.OnClickListener actionClick) {
        if (checkRootView()) {
            return T.showSnacke(mRootView.get(), msg, action, actionClick);
        }
        return null;
    }
}
