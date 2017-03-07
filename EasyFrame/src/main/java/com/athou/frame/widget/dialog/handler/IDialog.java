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

/**
 * Created by athou on 2016/10/8.
 */

public interface IDialog<T> {
    /**
     * 设置标题文字
     *
     * @return
     */
    T setTitle(String title);

    /**
     * 设置标题文字
     *
     * @param titleId
     * @return
     */
    T setTitle(int titleId);

    /**
     * 设置内容文字
     *
     * @param message
     * @return
     */
    T setMessage(String message);

    /**
     * 设置内容文字
     *
     * @param messageId
     * @return
     */
    T setMessage(int messageId);

    /**
     * 设置是否可以取消
     *
     * @param flag
     * @return
     */
    T setCancelable(boolean flag);

    /**
     * 设置是否点击外围取消
     *
     * @param cancel
     * @return
     */
    T setCanceledOnTouchOutside(boolean cancel);

    /**
     * 设置取消的监听
     *
     * @param listener
     * @return
     */
    T setOnCancelListener(DialogInterface.OnCancelListener listener);

    /**
     * 设置展示的监听
     * @param listener
     * @return
     */
    T setOnShowListener(DialogInterface.OnShowListener listener);

    /**
     * 设置隐藏的监听
     * @param listener
     * @return
     */
    T setOnDismissListener(DialogInterface.OnDismissListener listener);

    /**
     * 展示dialog
     *
     * @return
     */
    T show();

    /**
     * 是否正在展示
     *
     * @return
     */
    boolean isShowing();

    /**
     * 隐藏dialog
     */
    void dismiss();

    /**
     * 取消dialog
     */
    void cancel();
}
