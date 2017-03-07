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

package com.athou.frame.helper;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;

import com.athou.frame.util.L;

import java.lang.ref.WeakReference;

/**
 * 键盘工具类， 计算键盘高度
 * Created by athou on 2017/1/12.
 */

public class KeyboardHelper {

    private int sreenHeight = 0;
    private int sKeyBoardHeight = 0;
    private boolean mIsSoftKeyboardShowing = false;

    private WeakReference<View> decorView;
    private OnKeyboardStateChangeListener listener;

    public KeyboardHelper(Activity activity) {
        this.decorView = new WeakReference<View>(activity.getWindow().getDecorView());

        sreenHeight = activity.getResources().getDisplayMetrics().heightPixels;
        sKeyBoardHeight = sreenHeight / 3;
    }

    public void detackKeybord(final OnKeyboardStateChangeListener listener) {
        this.listener = listener;
        decorView.get().getViewTreeObserver().addOnGlobalLayoutListener(mLayoutChangeListener);
    }

    private ViewTreeObserver.OnGlobalLayoutListener mLayoutChangeListener = new ViewTreeObserver.OnGlobalLayoutListener() {

        @Override
        public void onGlobalLayout() {
            //判断窗口可见区域大小
            final Rect r = new Rect();
            decorView.get().getWindowVisibleDisplayFrame(r);
            //如果屏幕高度和Window可见区域高度差值大于整个屏幕高度的1/3，则表示软键盘显示中，否则软键盘为隐藏状态。
            int heightDiff = sreenHeight - (r.bottom - r.top);
            boolean isKeyboardShowing = heightDiff >= sKeyBoardHeight / 3;

            //如果之前软键盘状态为显示，现在为关闭，或者之前为关闭，现在为显示，则表示软键盘的状态发生了改变
            if ((mIsSoftKeyboardShowing && !isKeyboardShowing) || (!mIsSoftKeyboardShowing && isKeyboardShowing)) {
                mIsSoftKeyboardShowing = isKeyboardShowing;

                L.i("keyboardheight:" + heightDiff);
                if (listener != null) {
                    listener.onKeyboardStateChanged(mIsSoftKeyboardShowing, heightDiff);
                }
            }
        }
    };

    public void onDestroy() {
        //移除布局变化监听
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            decorView.get().getViewTreeObserver().removeOnGlobalLayoutListener(mLayoutChangeListener);
        } else {
            decorView.get().getViewTreeObserver().removeGlobalOnLayoutListener(mLayoutChangeListener);
        }
    }

    public interface OnKeyboardStateChangeListener {
        void onKeyboardStateChanged(boolean isKeyBoardShow, int keyboardHeight);
    }
}
