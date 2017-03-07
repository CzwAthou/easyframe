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

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.view.View.MeasureSpec;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by athou on 2017/1/12.
 */

public class ScreenShotHelper {

    private int cacheBitmapKey = 0x101;
    private int cacheBitmapDirtyKey = 0x012;

    private Bitmap.Config bitmap_quality = Bitmap.Config.RGB_565;

    private boolean quick_cache = false;
    private int color_background = Color.parseColor("#ff000000");

    public Bitmap getMagicDrawingCache(View view) {
        cacheBitmapKey = view.getId();
        cacheBitmapDirtyKey = cacheBitmapKey + 1;

        Bitmap bitmap = (Bitmap) view.getTag(cacheBitmapKey);
        Boolean dirty = (Boolean) view.getTag(cacheBitmapDirtyKey);
        if (view.getWidth() + view.getHeight() == 0) {
            view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        }
        int viewWidth = view.getWidth();
        int viewHeight = view.getHeight();
        if (bitmap == null || bitmap.getWidth() != viewWidth || bitmap.getHeight() != viewHeight) {
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
            bitmap = Bitmap.createBitmap(viewWidth, viewHeight, bitmap_quality);
            view.setTag(cacheBitmapKey, bitmap);
            dirty = true;
        }
        if (dirty == true || !quick_cache) {
            bitmap.eraseColor(color_background);
            Canvas canvas = new Canvas(bitmap);
            view.draw(canvas);
            view.setTag(cacheBitmapDirtyKey, false);
        }
        return bitmap;
    }

    /**
     * 截屏方法
     *
     * @param view
     */
    public static Bitmap takeScreenShot(View view) {
        /**
         * 我们要获取它的cache先要通过setDrawingCacheEnable方法把cache开启，
         * 然后再调用getDrawingCache方法就可以获得view的cache图片了。
         * buildDrawingCache方法可以不用调用，因为调用getDrawingCache方法时，
         * 若果cache没有建立，系统会自动调用buildDrawingCache方法生成cache。 若果要更新cache,
         * 必须要调用destoryDrawingCache方法把旧的cache销毁， 才能建立新的。
         */
//        view.setDrawingCacheEnabled(true);// 开启获取缓存
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();// 得到View的cache
        return bitmap;
    }

    /**
     * 截屏方法
     *
     * @param view
     * @param dir
     */
    public static String takeScreenShot(View view, String dir) {
        Bitmap bitmap = takeScreenShot(view);
        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint();
        paint.setColor(Color.YELLOW);
        SimpleDateFormat simple = new SimpleDateFormat("yyyyMMddhhmmss");
        String time = simple.format(new Date());
        canvas.save();
        canvas.restore();

        FileOutputStream fos = null;
        try {
            File file = new File(dir, "screen_shot_" + time + ".jpg");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();

            fos = new FileOutputStream(file);
            if (fos != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.close();
                return file.getAbsolutePath();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bitmap != null) {
                bitmap.recycle();
            }
            view.setDrawingCacheEnabled(false);
            view.destroyDrawingCache();
        }
        return null;
    }
}
