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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;

import com.athou.frame.widget.dialog.handler.ICustomDialog;

import java.lang.ref.WeakReference;

/**
 * Created by athou on 2016/10/8.
 */

public final class AlertDialogSample implements ICustomDialog<AlertDialogSample> {

    private WeakReference<Context> mContext;
    private AlertDialog dialog = null;

    public AlertDialogSample(Context context) {
        this.mContext = new WeakReference<Context>(context);
        this.dialog = new AlertDialog.Builder(context).create();
    }

    @Override
    public AlertDialogSample setBackgroundColor(int color) {
        return this;
    }

    @Override
    public AlertDialogSample setBackgroundResource(int resid) {
        return this;
    }

    @Override
    public AlertDialogSample setTitleHeight(int height) {
        return this;
    }

    @Override
    public AlertDialogSample setBottomHeight(int height) {
        return this;
    }

    @Override
    public AlertDialogSample setTitle(String title) {
        dialog.setTitle(title);
        return this;
    }

    @Override
    public AlertDialogSample setTitle(int titleId) {
        dialog.setTitle(titleId);
        return this;
    }

    @Override
    public AlertDialogSample setSummaryTitle(String summaryTitle) {
        return this;
    }

    @Override
    public AlertDialogSample setSummaryTitle(int titleId) {
        return this;
    }

    @Override
    public AlertDialogSample setTitleColor(int color) {
        return this;
    }

    @Override
    public AlertDialogSample setTitleSize(float textSize) {
        return this;
    }

    @Override
    public AlertDialogSample setMessage(String message) {
        dialog.setMessage(message);
        return this;
    }

    @Override
    public AlertDialogSample setMessage(int messageId) {
        dialog.setMessage(mContext.get().getString(messageId));
        return this;
    }

    @Override
    public AlertDialogSample setCancelable(boolean flag) {
        dialog.setCancelable(flag);
        return this;
    }

    @Override
    public AlertDialogSample setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }

    @Override
    public AlertDialogSample setOnCancelListener(DialogInterface.OnCancelListener listener) {
        dialog.setOnCancelListener(listener);
        return this;
    }

    @Override
    public AlertDialogSample setOnShowListener(DialogInterface.OnShowListener listener) {
        dialog.setOnShowListener(listener);
        return this;
    }

    @Override
    public AlertDialogSample setOnDismissListener(DialogInterface.OnDismissListener listener) {
        dialog.setOnDismissListener(listener);
        return this;
    }

    @Override
    public AlertDialogSample setMessageColor(int color) {
        return this;
    }

    @Override
    public AlertDialogSample setMessageSize(float textSize) {
        return this;
    }

    @Override
    public AlertDialogSample setMessageGravity(int gravity) {
        return this;
    }

    @Override
    public AlertDialogSample setPositiveButton(String text, DialogInterface.OnClickListener l) {
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, text, l);
        return this;
    }

    @Override
    public AlertDialogSample setPositiveButton(String text, int bgResId, DialogInterface.OnClickListener l) {
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, text, l);
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setBackgroundResource(bgResId);
        return this;
    }

    @Override
    public AlertDialogSample setPositiveButtonTextColor(int color) {
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(color);
        return this;
    }

    @Override
    public AlertDialogSample setPositiveButtonTextColor(ColorStateList color) {
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(color);
        return this;
    }

    @Override
    public AlertDialogSample setPositiveButtonTextSize(float textSize) {
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextSize(textSize);
        return this;
    }

    @Override
    public AlertDialogSample setNegativeButton(String text, DialogInterface.OnClickListener l) {
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, text, l);
        return this;
    }

    @Override
    public AlertDialogSample setNegativeButton(String text, int bgResId, DialogInterface.OnClickListener l) {
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, text, l);
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setBackgroundResource(bgResId);
        return this;
    }

    @Override
    public AlertDialogSample setNegativeButtonTextColor(int color) {
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(color);
        return this;
    }

    @Override
    public AlertDialogSample setNegativeButtonTextColor(ColorStateList color) {
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(color);
        return this;
    }

    @Override
    public AlertDialogSample setNegativeButtonTextSize(float textSize) {
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextSize(textSize);
        return this;
    }

    @Override
    public AlertDialogSample setCustomContent(View contentView) {
        dialog.setContentView(contentView);
        return this;
    }

    @Override
    public AlertDialogSample setCustomContent(View contentView, ViewGroup.LayoutParams layoutParams) {
        dialog.setContentView(contentView, layoutParams);
        return this;
    }

    @Override
    public AlertDialogSample show() {
        dialog.show();
        return this;
    }

    @Override
    public AlertDialogSample show(int width, int height) {
        if (dialog.getWindow() != null && dialog.getWindow().getDecorView() != null) {
            dialog.show();
            dialog.getWindow().setLayout(width, height);
        }
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

    @Override
    public void contentClick(View view) {
    }
}
