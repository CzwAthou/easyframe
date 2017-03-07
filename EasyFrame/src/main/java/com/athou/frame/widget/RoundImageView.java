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
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.athou.frame.R;

/**
 * Created by athou on 2016/10/24.
 */

public class RoundImageView extends ImageView {
    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
    private static final int COLORDRAWABLE_DIMENSION = 1;

    int mRadius = 30;
    int width = 0;
    int height = 0;

    int mGravity = Gravity.NONE;

    private Bitmap mBitmap = null;
    private BitmapShader mBitmapShader;
    private int mBitmapWidth;
    private int mBitmapHeight;

    private Path mPath;
    private Paint mBoundPaint;

    private boolean mReady;
    private boolean mSetupPending;

    public RoundImageView(Context context) {
        super(context);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView, defStyleAttr, 0);
        mGravity = a.getInt(R.styleable.RoundImageView_roundimageview_gravity, Gravity.NONE);
        a.recycle();

        mReady = true;
        if (mSetupPending) {
            setup();
            mSetupPending = false;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setup();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (getDrawable() == null) {
            return;
        }
        canvas.drawPath(mPath, mBoundPaint);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        mBitmap = bm;
        setup();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        mBitmap = getBitmapFromDrawable(drawable);
        setup();
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        mBitmap = getBitmapFromDrawable(getDrawable());
        setup();
    }

    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        try {
            Bitmap bitmap = null;

            if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION, COLORDRAWABLE_DIMENSION, BITMAP_CONFIG);
            } else {
                try {
                    bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                            BITMAP_CONFIG);
                } catch (Exception e) {
                    return null;
                }
            }

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }

    private void setup() {
        if (!mReady) {
            mSetupPending = true;
            return;
        }

        if (mBitmap == null) {
            return;
        }

        mBoundPaint = new Paint();
        mBoundPaint.setAntiAlias(true);
        mBoundPaint.setShader(mBitmapShader = new BitmapShader(mBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));

        mBitmapHeight = mBitmap.getHeight();
        mBitmapWidth = mBitmap.getWidth();
        mDrawableRect.set(0, 0, getWidth(), getHeight());

        mPath = new Path();
        mPath.moveTo(width / 2, 0);
        if (hasGravity(Gravity.Right_Top)) {
            mPath.lineTo(width - mRadius, 0);
            mPath.cubicTo(width - mRadius, 0, width, 0, width, mRadius);
        } else {
            mPath.lineTo(width, 0);
        }
        if (hasGravity(Gravity.Right_Bottom)) {
            mPath.lineTo(width, height - mRadius);
            mPath.cubicTo(width, height - mRadius, width, height, width - mRadius, height);
        } else {
            mPath.lineTo(width, height);
        }
        if (hasGravity(Gravity.Left_Bottom)) {
            mPath.lineTo(mRadius, height);
            mPath.cubicTo(mRadius, height, 0, height, 0, height - mRadius);
        } else {
            mPath.lineTo(0, height);
        }
        if (hasGravity(Gravity.Left_Top)) {
            mPath.lineTo(0, mRadius);
            mPath.cubicTo(0, mRadius, 0, 0, mRadius, 0);
        } else {
            mPath.lineTo(0, 0);
        }
        mPath.lineTo(mRadius, 0);

        updateShaderMatrix();
        invalidate();
    }

    private final Matrix mShaderMatrix = new Matrix();

    private final RectF mDrawableRect = new RectF();

    private void updateShaderMatrix() {
        float scale;
        float dx = 0;
        float dy = 0;

        mShaderMatrix.set(null);

        if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width() * mBitmapHeight) {
            scale = mDrawableRect.height() / (float) mBitmapHeight;
            dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f;
        } else {
            scale = mDrawableRect.width() / (float) mBitmapWidth;
            dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f;
        }

        mShaderMatrix.setScale(scale, scale);
        mShaderMatrix.postTranslate((int) (dx + 0.5f), (int) (dy + 0.5f));

        mBitmapShader.setLocalMatrix(mShaderMatrix);
    }

    private boolean hasGravity(int gravity) {
        return (mGravity & gravity) == gravity;
    }

    static class Gravity {
        static int NONE = 0b0000;
        static int Left_Top = 0b0001;
        static int Right_Top = 0b0010;
        static int Right_Bottom = 0b0100;
        static int Left_Bottom = 0b1000;
    }
}
