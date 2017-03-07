/*
 * Copyright (c) 2016.
 * 丸子地球 版权所有
 * 杭州幻橙网络科技有限公司
 * HuanCheng Network Technology CO. LTD ALL RIGHTS RESERVED
 */

package com.athou.frame.constants;

/**
 * app全局变量，包含配置变量
 */
public class AbAppData {

	/** 日志开关. */
	public static boolean DEBUG = false;

	/** 性能测试. */
	public static boolean mMonitorOpened = false;

	/** 起始执行时间. */
	public static long startLogTimeInMillis = 0;

	/** UI设计的基准宽度. */
	public static int UI_WIDTH = 720;

	/** UI设计的基准高度. */
	public static int UI_HEIGHT = 1280;

	/** UI设计的密度. */
	public static int UI_DENSITY = 2;

	/** 键盘显示&隐藏广播 */
	public static final String ACTION_KEYBOARD = "keyboard_show_hide";

}
