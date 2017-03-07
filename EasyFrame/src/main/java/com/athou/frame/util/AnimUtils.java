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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

public class AnimUtils {

	public static Animation getRotateAnimation(float fromDegrees, float toDegrees, long durationMillis) {
		RotateAnimation rotate = new RotateAnimation(fromDegrees, toDegrees, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		rotate.setDuration(durationMillis);
		rotate.setFillAfter(true);
		return rotate;
	}

	public static Animation getAlphaAnimation(float fromAlpha, float toAlpha, long durationMillis) {
		AlphaAnimation alpha = new AlphaAnimation(fromAlpha, toAlpha);
		alpha.setDuration(durationMillis);
		alpha.setFillAfter(true);
		return alpha;
	}

	public static Animation getScaleAnimation(long durationMillis) {
		ScaleAnimation scale = new ScaleAnimation(1.0f, 1.5f, 1.0f, 1.5f, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		scale.setDuration(durationMillis);
		scale.setInterpolator(new LinearInterpolator());
		return scale;
	}

	public static Animation getTranslateAnimation(float fromXDelta, float toXDelta, float fromYDelta, float toYDelta,
			long durationMillis) {
		TranslateAnimation translate = new TranslateAnimation(fromXDelta, toXDelta, fromYDelta, toYDelta);
		translate.setDuration(durationMillis);
		translate.setFillAfter(true);
		return translate;
	}

	public static Animation rotate(View view, float fromDegrees, float toDegrees, int durationMillis) {
		Animation animation = getRotateAnimation(fromDegrees, toDegrees, durationMillis);
		view.startAnimation(animation);
		return animation;
	}

	public static Animation scale(View v, int durationMillis) {
		Animation animation = getScaleAnimation(durationMillis);
		v.startAnimation(animation);
		return animation;
	}

    /**
     * 晃动动画
     *
     * @param counts
     *            1秒钟晃动多少下
     */
    public static Animation shakeAnimation(int counts) {
        Animation translateAnimation = new TranslateAnimation(0, 6, 0, 0);
        translateAnimation.setInterpolator(new CycleInterpolator(counts));
        translateAnimation.setDuration(500);
        return translateAnimation;
    }
}
