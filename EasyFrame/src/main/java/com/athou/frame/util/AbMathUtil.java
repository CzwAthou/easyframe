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

import android.graphics.PointF;
import android.view.MotionEvent;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 描述：数学处理类.
 * 
 * @author zhaoqp
 * @date：2013-1-18 上午10:14:44
 * @version v1.0
 */
public class AbMathUtil {

	/**
	 * 获取位数
	 * @param num
	 * @return
     */
	public static int getBitNum(double num) {
		return (int) Math.log10(num) + 1;
	}

	/**
	 * 四舍五入.
	 * 
	 * @param number
	 *            原数
	 * @param decimal
	 *            保留几位小数
	 * @return 四舍五入后的值
	 */
	public static BigDecimal round(double number, int decimal) {
		return new BigDecimal(number).setScale(decimal, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * 二进制转为十六进制
	 */
	public static char binaryToHex(int binary) {
		char ch = ' ';
		switch (binary) {
		case 0:
			ch = '0';
			break;
		case 1:
			ch = '1';
			break;
		case 2:
			ch = '2';
			break;
		case 3:
			ch = '3';
			break;
		case 4:
			ch = '4';
			break;
		case 5:
			ch = '5';
			break;
		case 6:
			ch = '6';
			break;
		case 7:
			ch = '7';
			break;
		case 8:
			ch = '8';
			break;
		case 9:
			ch = '9';
			break;
		case 10:
			ch = 'a';
			break;
		case 11:
			ch = 'b';
			break;
		case 12:
			ch = 'c';
			break;
		case 13:
			ch = 'd';
			break;
		case 14:
			ch = 'e';
			break;
		case 15:
			ch = 'f';
			break;
		default:
			ch = ' ';
		}
		return ch;
	}

	/**
	 * 计算数组的平均值
	 * 
	 * @param pixels
	 *            数组
	 * @return int 平均值
	 */
	public static int average(int[] pixels) {
		float m = 0;
		for (int i = 0; i < pixels.length; ++i) {
			m += pixels[i];
		}
		m = m / pixels.length;
		return (int) m;
	}

	/**
	 * 计算数组的平均值
	 * 
	 * @param pixels
	 *            数组
	 * @return int 平均值
	 */
	public static int average(double[] pixels) {
		float m = 0;
		for (int i = 0; i < pixels.length; ++i) {
			m += pixels[i];
		}
		m = m / pixels.length;
		return (int) m;
	}


	/**
	 * 补全小数点后2位
	 */
	public static String formatFigureTwoPlaces(float format) {
		DecimalFormat fnum = new DecimalFormat("##0.00");
		return fnum.format(format);
	}

	/**
	 * 补全小数点后N位（空位补0）
	 * 
	 * @param numResource
	 *            原数字
	 * @param numPlaces
	 *            几位小数点
	 * @return
	 */
	public static String formatFigureNPlaces(float numResource, int numPlaces) {
		String n = String.valueOf(Math.pow(10, numPlaces));
		String format = "##0." + n.substring(1, numPlaces);
		DecimalFormat fnum = new DecimalFormat(format);
		return fnum.format(numResource);
	}

	/**
	 * 补全2位整数（空位补0）
	 * 
	 * @param numResource
	 * @return
	 */
	public static String formatIntegerTwoPlaces(int numResource) {
		DecimalFormat fnum = new DecimalFormat("##00");
		return fnum.format(numResource);
	}

	/**
	 * 补全整数（空位补0）
	 * 
	 * @param numResource
	 *            原数字
	 * @param numPlaces
	 *            几位整数
	 * @return
	 */
	public synchronized static String formatIntegerNPlaces(int numResource, int numPlaces) {
		// NumberFormat f =
		// NumberFormat.getIntegerInstance(Locale.getDefault());
		// if (f instanceof DecimalFormat) {
		// ((DecimalFormat) f).setDecimalSeparatorAlwaysShown(false);
		// ((DecimalFormat) f).applyPattern(getNumZerosString(numPlaces));
		// return f.format(numResource);
		// } else {
		String n = "";
		for (int i = numPlaces; i >= 1; i--) {
			if (numResource >= (int) Math.pow(10, i - 1)) {
				n = getNumZerosString(numPlaces - i) + numResource;
				break;
			} else {
				continue;
			}
		}
		return n;
		// if (numResource >= 1000) {
		// return "" + numResource;
		// } else if (numResource >= 100) {
		// return "0" + numResource;
		// } else if (numResource >= 10) {
		// return "00" + numResource;
		// } else {
		// return "000" + numResource;
		// }
		// }
	}

	private static String getNumZerosString(int numZero) {
		String n = String.valueOf((int) Math.pow(10, numZero));
		return n.substring(1);
	}

	/**
	 * 判断数组中是否包含某元素
	 * 
	 * @author 菜菜
	 * @param array
	 * @param v
	 * @return
	 * 
	 */
	public static <T> boolean contains(final T[] array, final T v) {
		for (final T e : array)
			if (e == v || v != null && v.equals(e))
				return true;
		return false;
	}

    public static float distance(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    public static float distance(PointF p1, PointF p2) {
        float x = p1.x - p2.x;
        float y = p1.y - p2.y;
        return (float) Math.sqrt(x * x + y * y);
    }

    public static float distance(float x1, float y1, float x2, float y2) {
        float x = x1 - x2;
        float y = y1 - y2;
        return (float) Math.sqrt(x * x + y * y);
    }

    public static void midpoint(MotionEvent event, PointF point) {
        float x1 = event.getX(0);
        float y1 = event.getY(0);
        float x2 = event.getX(1);
        float y2 = event.getY(1);
        midpoint(x1, y1, x2, y2, point);
    }

    public static void midpoint(float x1, float y1, float x2, float y2, PointF point) {
        point.x = (x1 + x2) / 2.0f;
        point.y = (y1 + y2) / 2.0f;
    }
    /**
     * Rotates p1 around p2 by angle degrees.
     * @param p1
     * @param p2
     * @param angle
     */
    public void rotate(PointF p1, PointF p2, float angle) {
        float px = p1.x;
        float py = p1.y;
        float ox = p2.x;
        float oy = p2.y;
        p1.x = (float) (Math.cos(angle) * (px-ox) - Math.sin(angle) * (py-oy) + ox);
        p1.y = (float) (Math.sin(angle) * (px-ox) + Math.cos(angle) * (py-oy) + oy);
    }

    public static float angle(PointF p1, PointF p2) {
        return angle(p1.x, p1.y, p2.x, p2.y);
    }

    public static float angle(float x1, float y1, float x2, float y2) {
        return (float) Math.atan2(y2 - y1, x2 - x1);
    }
}
