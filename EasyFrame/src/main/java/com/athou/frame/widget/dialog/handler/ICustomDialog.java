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

package com.athou.frame.widget.dialog.handler;

import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by athou on 2016/10/8.
 */

public interface ICustomDialog<T> extends IDialog<T>{
    /**
     * 设置dialog背景色
     * @param color
     * @return
     */
    T setBackgroundColor(int color);

    /**
     * 设置dialog背景图片
     * @param resid
     * @return
     */
    T setBackgroundResource(int resid);

    /**
     * 设置标题栏高度
     * @param height
     * @return
     */
    T setTitleHeight(int height);

    /**
     * 设置底部按钮高度
     * @param height
     * @return
     */
    T setBottomHeight(int height);

    /**
     * 设置副标题文字
     * @param summaryTitle
     * @return
     */
    T setSummaryTitle(String summaryTitle);

    /**
     * 设置副标题文字
     * @param titleId
     * @return
     */
    T setSummaryTitle(int titleId);

    /**
     * 设置标题文字颜色
     * @param color
     * @return
     */
    T setTitleColor(int color);

    /**
     * 设置标题文字大小
     * @param textSize
     * @return
     */
    T setTitleSize(float textSize);

    /**
     * 设置内容文字颜色
     * @param color
     * @return
     */
    T setMessageColor(int color);

    /**
     * 设置内容文字大小
     * @param textSize
     * @return
     */
    T setMessageSize(float textSize);

    /**
     * 设置内容文字Gravity
     * @param gravity
     * @return
     */
    T setMessageGravity(int gravity);

    /**
     * 设置PositiveButton
     * @param text
     * @param l
     * @return
     */
    T setPositiveButton(String text, DialogInterface.OnClickListener l);

    /**
     * 设置PositiveButton, 附加按钮背景
     * @param text
     * @param bgResId
     * @param l
     * @return
     */
    T setPositiveButton(String text, int bgResId, DialogInterface.OnClickListener l);

    /**
     * 设置PositiveButton文本颜色
     * @param color
     * @return
     */
    T setPositiveButtonTextColor(int color);

    /**
     * 设置PositiveButton文本颜色
     * @param color
     * @return
     */
    T setPositiveButtonTextColor(ColorStateList color);

    /**
     * 设置PositiveButton文本大小
     * @param textSize
     * @return
     */
    T setPositiveButtonTextSize(float textSize);

    /**
     * 设置NegativeButton
     * @param text
     * @param l
     * @return
     */
    T setNegativeButton(String text, DialogInterface.OnClickListener l);

    /**
     * 设置NegativeButton，附加按钮背景
     * @param text
     * @param bgResId
     * @param l
     * @return
     */
    T setNegativeButton(String text, int bgResId, DialogInterface.OnClickListener l);

    /**
     * 设置NegativeButton文本颜色
     * @param color
     * @return
     */
    T setNegativeButtonTextColor(int color);

    /**
     * 设置NegativeButton文本颜色
     * @param color
     * @return
     */
    T setNegativeButtonTextColor(ColorStateList color);

    /**
     * 设置NegativeButton文本大小
     * @param textSize
     * @return
     */
    T setNegativeButtonTextSize(float textSize);

    /**
     * 设置contentView ，且默认的LayoutParams是
     * LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
     *
     * @param contentView
     * @return
     */
    T setCustomContent(View contentView);

    /**
     * 设置contentView
     *
     * @param contentView
     * @return
     */
    T setCustomContent(View contentView, ViewGroup.LayoutParams layoutParams);

    /**
     * 按指定的大小展示dialog
     * @param width
     * @param height
     */
    T show(int width, int height);

    /**
     * contentview 点击事件的处理
     * @param view
     */
    void contentClick(View view);
}
