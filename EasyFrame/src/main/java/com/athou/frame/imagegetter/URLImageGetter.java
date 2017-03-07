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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.athou.frame.net.util.SyncHttp;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2015/11/15.
 */
public class URLImageGetter implements Html.ImageGetter {
    Context context;
    TextView textView;

    private int drawableWidth;
    private int drawableHeight;

    public URLImageGetter(Context context, TextView tv, int drawableWidth) {
        this.context = context;
        this.textView = tv;
        this.drawableWidth = drawableWidth;
    }

    public URLImageGetter(Context context, TextView tv) {
        this.context = context;
        this.textView = tv;
        this.textView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                textView.getViewTreeObserver().removeOnPreDrawListener(this);
                drawableWidth = textView.getWidth();
                return true;
            }
        });
    }

    @Override
    public Drawable getDrawable(String paramString) {
        final URLDrawable urlDrawable = new URLDrawable(context);

        ImageGetterAsyncTask getterTask = new ImageGetterAsyncTask(urlDrawable);
        getterTask.execute(paramString);
        return urlDrawable;
    }

    public class ImageGetterAsyncTask extends AsyncTask<String, Void, Drawable> {
        URLDrawable urlDrawable;

        public ImageGetterAsyncTask(URLDrawable drawable) {
            this.urlDrawable = drawable;
        }

        @Override
        protected void onPostExecute(Drawable result) {
            if (result != null) {
                urlDrawable.drawable = result;

                URLImageGetter.this.textView.requestLayout();
            }
        }

        @Override
        protected Drawable doInBackground(String... params) {
            String source = params[0];
            return fetchDrawable(source);
        }

        public Drawable fetchDrawable(String url) {
            try {
                InputStream is = SyncHttp.httpGet(url);
                Bitmap bitmapOrg = BitmapFactory.decodeStream(is);
                if (bitmapOrg == null) {
                    return null;
                }
//                Rect bounds = AbAppUtil.getDefaultImageBounds(context, URLDrawable.imageScale);
                int w = bitmapOrg.getWidth();
                int h = bitmapOrg.getHeight();
                Rect bounds = new Rect(0, 0, drawableWidth, drawableWidth * h / w);
                Bitmap bitmap = Bitmap.createScaledBitmap(bitmapOrg, bounds.right, bounds.bottom, true);
                BitmapDrawable drawable = new BitmapDrawable(context.getResources(), bitmap);
                drawable.setBounds(bounds);
//                Drawable drawable  = Drawable.createFromStream(is,"");
//                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                return drawable;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}