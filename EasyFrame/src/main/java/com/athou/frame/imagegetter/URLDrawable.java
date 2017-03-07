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

package com.athou.frame.imagegetter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.athou.frame.util.BitmapUtils;

/**
 * Created by Administrator on 2015/11/15.
 */
public class URLDrawable extends BitmapDrawable {
    protected Drawable drawable;

    public static float imageScale = 3.0f/4;

    public URLDrawable(Context context) {
        this.setBounds(BitmapUtils.getDefaultImageBounds(imageScale));

        drawable = context.getResources().getDrawable(android.R.drawable.stat_notify_error);
        drawable.setBounds(BitmapUtils.getDefaultImageBounds(imageScale));
    }

    @Override
    public void draw(Canvas canvas) {
        Log.d("test", "this=" + this.getBounds());
        if (drawable != null) {
            Log.d("test", "draw=" + drawable.getBounds());
            drawable.draw(canvas);
        }
    }

}