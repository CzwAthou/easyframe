/*
 * Copyright (c) 2016  athou（cai353974361@163.com）.
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

package com.athou.frame.widget.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.athou.frame.widget.dialog.handler.ILoadDialog;

import java.lang.ref.WeakReference;

/**
 * Created by athou on 2016/10/8.
 */

public final class LoadDialogSample implements ILoadDialog<LoadDialogSample> {

    private WeakReference<Context> mContext;
    private ProgressDialog dialog = null;

    public LoadDialogSample(Context context) {
        this.mContext = new WeakReference<Context>(context);
        this.dialog = new ProgressDialog(context);
        ;
    }

    @Override
    public LoadDialogSample setTitle(String title) {
        dialog.setTitle(title);
        return this;
    }

    @Override
    public LoadDialogSample setTitle(int titleId) {
        dialog.setTitle(titleId);
        return this;
    }

    @Override
    public LoadDialogSample setMessage(String msg) {
        dialog.setMessage(msg);
        return this;
    }

    @Override
    public LoadDialogSample setMessage(int messageId) {
        dialog.setMessage(mContext.get().getString(messageId));
        return this;
    }

    @Override
    public LoadDialogSample setCancelable(boolean flag) {
        dialog.setCancelable(flag);
        return this;
    }

    @Override
    public LoadDialogSample setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }

    @Override
    public LoadDialogSample setOnCancelListener(DialogInterface.OnCancelListener listener) {
        dialog.setOnCancelListener(listener);
        return this;
    }

    @Override
    public LoadDialogSample setOnShowListener(DialogInterface.OnShowListener listener) {
        dialog.setOnShowListener(listener);
        return this;
    }

    @Override
    public LoadDialogSample setOnDismissListener(DialogInterface.OnDismissListener listener) {
        dialog.setOnDismissListener(listener);
        return this;
    }


    @Override
    public LoadDialogSample show() {
        dialog.show();
        return this;
    }

    @Override
    public boolean isShowing() {
        if (dialog != null) {
            return dialog.isShowing();
        }
        return false;
    }

    @Override
    public void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void cancel() {
        if (dialog != null && dialog.isShowing()) {
            dialog.cancel();
        }
    }
}
