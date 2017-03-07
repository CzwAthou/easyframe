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

package com.athou.frame.util;

import android.view.View;

import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;

/**
 * Created by athou on 2016/10/8.
 */
public class ClickUtil {

    // 双击事件记录最近一次点击的时间
    private static long lastClickTime = 0;

    /**
     * 按钮点击去重实现方法一 ，需要在onclick回调内调用
     */
    public static boolean isClickAvalible() {
        if (Math.abs(lastClickTime - System.currentTimeMillis()) < 500) {
            lastClickTime = 0;
            return false;
        } else {
            lastClickTime = System.currentTimeMillis();
            return true;
        }
    }

    /**
     * 按钮点击去重实现方法二，直接调用
     *
     * @param view
     * @param action1
     */
    public static void RegistClick(View view, Action1 action1) {
        RxView.clicks(view)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(action1);
    }

    /**
     * 按钮点击去重实现方法二，直接调用
     *
     * @param view
     * @param action1
     */
    public static void RegistLongClick(View view, Action1 action1) {
        RxView.longClicks(view)
                .subscribe(action1);
    }
}
