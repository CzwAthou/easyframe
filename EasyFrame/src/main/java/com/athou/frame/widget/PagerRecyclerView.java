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

package com.athou.frame.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.athou.frame.util.L;

/**
 * RecyclerView实现viewapger翻页功能
 * Created by athou on 2016/11/1.
 */

public class PagerRecyclerView extends RecyclerView {

    GestureDetector detector;

    public PagerRecyclerView(Context context) {
        super(context);
        init(context);
    }

    public PagerRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PagerRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        detector = new GestureDetector(context, gestureListener);
        this.setOnTouchListener(onTouchListener);
    }

    int currentPosition = 0;
    private GestureDetector.OnGestureListener gestureListener = new GestureDetector.OnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            L.i("onFling");
            if (e1.getX() - e2.getX() > 0 && currentPosition < getChildCount()) {
                currentPosition++;
                // 手向左滑动，图片下一张
            } else if (e2.getX() - e1.getX() > 0 && currentPosition > 0) {
                // 向右滑动，图片上一张
                currentPosition--;
            }
            smoothScrollToPosition(currentPosition);
            return false;
        }
    };

    private OnTouchListener onTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            Log.i("phc", "onTouch");
            //传递event给GestureDetector对象
            detector.onTouchEvent(motionEvent);
            return true;
        }
    };
}
