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

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.athou.frame.R;
import com.athou.frame.widget.dialog.handler.ICustomDialog;

/**
 * 仿IOS dialog
 *
 * @author 菜菜
 * @date 2015/02/10
 */
public class FinalDialog implements ICustomDialog<FinalDialog>, View.OnClickListener {

    protected Context mContext;

    protected Dialog dialog;
    private View main_bg;

    private LinearLayout titleLayout;
    private TextView tv_title;
    private TextView tv_summary_title;

    private LinearLayout contentLayout;
    private TextView tv_msg;

    protected LinearLayout bottomLayout;
    private View view_devide;
    protected Button btn_cancel;
    protected Button btn_ok;

    private String titleText = null;
    private String summaryTitleText = null;

    private String mPositiveText = null;
    private String mNegativeText = null;
    private DialogInterface.OnClickListener mPositiveButtonListener = null;
    private DialogInterface.OnClickListener mNegativeButtonListener = null;

    public FinalDialog(Context context) {
        initView(context, R.style.dialog_content);
    }

    public FinalDialog(Context context, int theme) {
        initView(context, theme);
    }

    private final void initView(Context context, int theme) {
        mContext = context;
        dialog = new Dialog(mContext, theme);

        View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_custom_layout, null);

        main_bg = v.findViewById(R.id.dialog_main);

        titleLayout = (LinearLayout) v.findViewById(R.id.custom_dialog_title_layout);
        tv_title = (TextView) v.findViewById(R.id.custom_dialog_title_tv);
        tv_summary_title = (TextView) v.findViewById(R.id.custom_dialog_title_summary_tv);

        contentLayout = (LinearLayout) v.findViewById(R.id.custom_dialog_content_layout);
        tv_msg = (TextView) v.findViewById(R.id.custom_dialog_content_tv);
        tv_msg.setMovementMethod(ScrollingMovementMethod.getInstance());

        bottomLayout = (LinearLayout) v.findViewById(R.id.custom_dialog_bottom_layout);
        view_devide = v.findViewById(R.id.dialog_devide_line);
        btn_cancel = (Button) v.findViewById(R.id.btn_dialog_cancel);
        btn_ok = (Button) v.findViewById(R.id.btn_dialog_ok);

        btn_cancel.setOnClickListener(this);
        btn_ok.setOnClickListener(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(v);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
    }

    public Dialog getDialog() {
        return dialog;
    }

    @Override
    public FinalDialog setBackgroundColor(int color) {
        main_bg.setBackgroundColor(color);
        return this;
    }

    @Override
    public FinalDialog setBackgroundResource(int resid) {
        main_bg.setBackgroundResource(resid);
        return this;
    }

    @Override
    public FinalDialog setTitleHeight(int height) {
        LayoutParams lp = titleLayout.getLayoutParams();
        lp.height = height;
        titleLayout.setLayoutParams(lp);
        return this;
    }

    @Override
    public FinalDialog setBottomHeight(int height) {
        LayoutParams layoutParams = bottomLayout.getLayoutParams();
        layoutParams.height = height;
        bottomLayout.setLayoutParams(layoutParams);
        return this;
    }

    @Override
    public FinalDialog setTitle(String title) {
        titleText = title;
        return this;
    }

    @Override
    public FinalDialog setTitle(int titleId) {
        titleText = getResources().getString(titleId);
        return this;
    }

    @Override
    public FinalDialog setSummaryTitle(String summaryTitle) {
        summaryTitleText = summaryTitle;
        return this;
    }

    @Override
    public FinalDialog setSummaryTitle(int titleId) {
        summaryTitleText = getResources().getString(titleId);
        return this;
    }

    @Override
    public FinalDialog setTitleColor(int color) {
        tv_title.setTextColor(color);
        return this;
    }

    @Override
    public FinalDialog setTitleSize(float textSize) {
        tv_title.setTextSize(textSize);
        return this;
    }

    @Override
    public FinalDialog setMessage(String message) {
        tv_msg.setText(message);
        return this;
    }

    @Override
    public FinalDialog setMessage(int messageId) {
        tv_msg.setText(messageId);
        return this;
    }

    @Override
    public FinalDialog setMessageColor(int color) {
        tv_msg.setTextColor(color);
        return this;
    }

    @Override
    public FinalDialog setMessageSize(float textSize) {
        tv_msg.setTextSize(textSize);
        return this;
    }

    @Override
    public FinalDialog setMessageGravity(int gravity) {
        tv_msg.setGravity(gravity);
        return this;
    }

    @Override
    public FinalDialog setPositiveButton(String text, DialogInterface.OnClickListener l) {
        mPositiveText = text;
        mPositiveButtonListener = l;
        return this;
    }

    @Override
    public FinalDialog setPositiveButton(String text, int backgroundId, DialogInterface.OnClickListener l) {
        btn_ok.setBackgroundResource(backgroundId);
        return setPositiveButton(text, l);
    }

    @Override
    public FinalDialog setPositiveButtonTextColor(int color) {
        btn_ok.setTextColor(color);
        return this;
    }

    @Override
    public FinalDialog setPositiveButtonTextColor(ColorStateList color) {
        btn_ok.setTextColor(color);
        return this;
    }

    @Override
    public FinalDialog setPositiveButtonTextSize(float textSize) {
        btn_ok.setTextSize(textSize);
        return this;
    }

    @Override
    public FinalDialog setNegativeButton(String text, DialogInterface.OnClickListener l) {
        mNegativeText = text;
        mNegativeButtonListener = l;
        return this;
    }

    @Override
    public FinalDialog setNegativeButton(String text, int backgroundId, DialogInterface.OnClickListener l) {
        btn_cancel.setBackgroundResource(backgroundId);
        return setNegativeButton(text, l);
    }

    @Override
    public FinalDialog setNegativeButtonTextColor(int color) {
        btn_cancel.setTextColor(color);
        return this;
    }

    @Override
    public FinalDialog setNegativeButtonTextColor(ColorStateList color) {
        btn_cancel.setTextColor(color);
        return this;
    }

    @Override
    public FinalDialog setNegativeButtonTextSize(float textSize) {
        btn_cancel.setTextSize(textSize);
        return this;
    }

    @Override
    public FinalDialog setCustomContent(View contentView) {
        if (contentView.getLayoutParams() == null) {
            contentView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        }
        contentLayout.removeAllViews();
        contentLayout.addView(contentView);
        return this;
    }

    @Override
    public FinalDialog setCustomContent(View contentView, LayoutParams layoutParams) {
        contentView.setLayoutParams(layoutParams);
        contentLayout.removeAllViews();
        contentLayout.addView(contentView);
        return this;
    }

    @Override
    public FinalDialog setCancelable(boolean flag) {
        dialog.setCancelable(flag);
        return this;
    }

    @Override
    public FinalDialog setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }

    @Override
    public FinalDialog setOnShowListener(DialogInterface.OnShowListener listener) {
        dialog.setOnShowListener(listener);
        return this;
    }

    @Override
    public FinalDialog setOnCancelListener(DialogInterface.OnCancelListener listener) {
        dialog.setOnCancelListener(listener);
        return this;
    }

    @Override
    public FinalDialog setOnDismissListener(DialogInterface.OnDismissListener listener) {
        dialog.setOnDismissListener(listener);
        return this;
    }

    protected void onStart() {
        if (null != mPositiveButtonListener && null != mNegativeButtonListener) {
            bottomLayout.setVisibility(View.VISIBLE);
            view_devide.setVisibility(View.VISIBLE);
            btn_ok.setVisibility(View.VISIBLE);
            btn_cancel.setVisibility(View.VISIBLE);

//            btn_cancel.setBackgroundResource(R.drawable.selector_white_gray_left_bottom);
//            btn_ok.setBackgroundResource(R.drawable.selector_white_gray_right_bottom);
        } else if (null != mPositiveButtonListener) {
            bottomLayout.setVisibility(View.VISIBLE);
            view_devide.setVisibility(View.GONE);
            btn_ok.setVisibility(View.VISIBLE);
            btn_cancel.setVisibility(View.GONE);

//            btn_ok.setBackgroundResource(R.drawable.selector_white_gray_bottom);
        } else if (null != mNegativeButtonListener) {
            bottomLayout.setVisibility(View.VISIBLE);
            view_devide.setVisibility(View.GONE);
            btn_ok.setVisibility(View.GONE);
            btn_cancel.setVisibility(View.VISIBLE);

//            btn_cancel.setBackgroundResource(R.drawable.selector_white_gray_bottom);
        } else {
            bottomLayout.setVisibility(View.GONE);
            view_devide.setVisibility(View.GONE);
            btn_ok.setVisibility(View.GONE);
            btn_cancel.setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(titleText)) {
            titleLayout.setVisibility(View.GONE);
        } else {
            titleLayout.setVisibility(View.VISIBLE);
            tv_title.setText(titleText);
        }
        if (TextUtils.isEmpty(summaryTitleText)) {
            tv_summary_title.setVisibility(View.GONE);
        } else {
            tv_summary_title.setVisibility(View.VISIBLE);
            tv_summary_title.setText(summaryTitleText);
        }
        btn_ok.setText(mPositiveText);
        btn_cancel.setText(mNegativeText);
    }

    @Override
    public void onClick(View v) {
        if (v == btn_ok) {
            if (null != mPositiveButtonListener) {
                mPositiveButtonListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
            }
        } else if (v == btn_cancel) {
            if (null != mNegativeButtonListener) {
                mNegativeButtonListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
            }
            dismiss();
        } else {
            contentClick(v);
        }
    }

    @Override
    public void contentClick(View view) {
    }

    @Override
    public FinalDialog show() {
        if (dialog.getWindow() != null && dialog.getWindow().getDecorView() != null) {
            onStart();
            dialog.show();
        }
        return this;
    }

    @Override
    public FinalDialog show(int width, int height) {
        if (dialog.getWindow() != null && dialog.getWindow().getDecorView() != null) {
            onStart();
            dialog.show();
            dialog.getWindow().setLayout(width, height);
        }
        return this;
    }

    @Override
    public boolean isShowing() {
        return dialog.isShowing();
    }

    @Override
    public void dismiss() {
        if (dialog.getWindow() != null && dialog.getWindow().getDecorView() != null) {
            hideSoftInputView();
            dialog.dismiss();
        }
        return;
    }

    @Override
    public void cancel() {
        if (dialog != null && dialog.isShowing()) {
            hideSoftInputView();
            dialog.cancel();
        }
    }

    private void hideSoftInputView() {
        if (mContext != null && mContext instanceof Activity) {
            InputMethodManager imm = ((InputMethodManager) mContext.getSystemService(Activity.INPUT_METHOD_SERVICE));
            View focusView = dialog.getCurrentFocus();
            if (imm != null && focusView != null) {
                imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
            }
        }
    }

    public Resources getResources() {
        return mContext.getResources();
    }
}
