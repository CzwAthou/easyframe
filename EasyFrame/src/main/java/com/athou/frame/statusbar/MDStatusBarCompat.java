///*
// * Copyright (c) 2016  athou（cai353974361@163.com）.
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package com.athou.frame.statusbar;
//
//import android.app.Activity;
//import android.os.Build;
//import android.support.design.widget.AppBarLayout;
//import android.support.design.widget.CollapsingToolbarLayout;
//import android.support.design.widget.CoordinatorLayout;
//import android.support.v4.content.ContextCompat;
//import android.support.v4.view.ViewCompat;
//import android.support.v7.widget.Toolbar;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.ImageView;
//
//import com.athou.frame.R;
//import com.athou.frame.util.AbAppUtil;
//import com.athou.frame.util.L;
//import com.athou.frame.util.OsInfoUtil;
//
//import java.lang.reflect.Field;
//import java.lang.reflect.Method;
//
///**
// * Created by athou on 2016/10/25.
// */
//public class MDStatusBarCompat {
//
//    /**
//     * 设置状态栏黑色字体图标，
//     * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
//     *
//     * @param activity
//     * @return 1:MIUUI 2:Flyme 3:android6.0
//     */
//    public static int StatusBarLightMode(Activity activity) {
//        StatusBarUtil.setStatusBarTextColor(activity, true);
//        int result = 0;
//        if (OsInfoUtil.isKitkat()) {
//            if (MIUISetStatusBarLightMode(activity.getWindow(), true)) {
//                result = 1;
//            } else if (FlymeSetStatusBarLightMode(activity.getWindow(), true)) {
//                result = 2;
//            } else if (OsInfoUtil.isM()) {
//                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//                result = 3;
//            } else { //其他系统
//            }
//        }
//        return result;
//    }
//
//    /**
//     * 已知系统类型时，设置状态栏黑色字体图标。
//     * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
//     *
//     * @param activity
//     * @param type     1:MIUUI 2:Flyme 3:android6.0
//     */
//    public static void StatusBarLightMode(Activity activity, int type) {
//        if (type == 1) {
//            MIUISetStatusBarLightMode(activity.getWindow(), true);
//        } else if (type == 2) {
//            FlymeSetStatusBarLightMode(activity.getWindow(), true);
//        } else if (type == 3) {
//            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//        }
//    }
//
//    /**
//     * 清除MIUI或flyme或6.0以上版本状态栏黑色字体
//     */
//    public static void StatusBarDarkMode(Activity activity, int type) {
//        if (type == 1) {
//            MIUISetStatusBarLightMode(activity.getWindow(), false);
//        } else if (type == 2) {
//            FlymeSetStatusBarLightMode(activity.getWindow(), false);
//        } else if (type == 3) {
//            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
//        }
//    }
//
//
//    /**
//     * 设置状态栏图标为深色和魅族特定的文字风格
//     * 可以用来判断是否为Flyme用户
//     *
//     * @param window 需要设置的窗口
//     * @param dark   是否把状态栏字体及图标颜色设置为深色
//     * @return boolean 成功执行返回true
//     */
//    private static boolean FlymeSetStatusBarLightMode(Window window, boolean dark) {
//        boolean result = false;
//        if (window != null) {
//            try {
//                WindowManager.LayoutParams lp = window.getAttributes();
//                Field darkFlag = WindowManager.LayoutParams.class
//                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
//                Field meizuFlags = WindowManager.LayoutParams.class
//                        .getDeclaredField("meizuFlags");
//                darkFlag.setAccessible(true);
//                meizuFlags.setAccessible(true);
//                int bit = darkFlag.getInt(null);
//                int value = meizuFlags.getInt(lp);
//                if (dark) {
//                    value |= bit;
//                } else {
//                    value &= ~bit;
//                }
//                meizuFlags.setInt(lp, value);
//                window.setAttributes(lp);
//                result = true;
//            } catch (Exception e) {
//                L.i("FlymeSetStatusBarLightMode none");
//            }
//        }
//        return result;
//    }
//
//    /**
//     * 设置状态栏字体图标为深色，需要MIUIV6以上
//     *
//     * @param window 需要设置的窗口
//     * @param dark   是否把状态栏字体及图标颜色设置为深色
//     * @return boolean 成功执行返回true
//     */
//    private static boolean MIUISetStatusBarLightMode(Window window, boolean dark) {
//        boolean result = false;
//        if (window != null) {
//            Class clazz = window.getClass();
//            try {
//                int darkModeFlag = 0;
//                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
//                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
//                darkModeFlag = field.getInt(layoutParams);
//                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
//                if (dark) {
//                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
//                } else {
//                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
//                }
//                result = true;
//            } catch (Exception e) {
//                L.i("MIUISetStatusBarLightMode none");
//            }
//        }
//        return result;
//    }
//
//    /**
//     * 简单型状态栏(ToolBar)
//     *
//     * @param activity
//     */
//    public static void setOrdinaryToolBar(Activity activity, int statusBarColorResId) {
//        if (OsInfoUtil.isLOLLIPOP()) {
//            Window window = activity.getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(ContextCompat.getColor(activity, statusBarColorResId));
//        } else if (OsInfoUtil.isKitkat()) {
//            setKKStatusBar(activity, statusBarColorResId);
//        }
//    }
//
//    /**
//     * 图片全屏透明状态栏（图片位于状态栏下面）
//     *
//     * @param activity
//     */
//    public static void setImageTransparent(Activity activity) {
////        if (OsInfoUtil.isLOLLIPOP()) {
////            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
////        }
//        setTranslucentStatus(activity, true);
//    }
//
//    /**
//     * 图片全屏半透明状态栏（图片位于状态栏下面）
//     *
//     * @param activity
//     */
//    public static void setImageTranslucent(Activity activity, int statusBarColorResId) {
//        if (OsInfoUtil.isLOLLIPOP()) {
//            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//            activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, statusBarColorResId));
//        } else {
//            setKKStatusBar(activity, statusBarColorResId);
//        }
//    }
//
//    /**
//     * set status bar translucency
//     */
//    public static void setTranslucentStatus(Activity activity, boolean on) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window win = activity.getWindow();
//            WindowManager.LayoutParams winParams = win.getAttributes();
//            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
//            if (on) {
//                winParams.flags |= bits;
//            } else {
//                winParams.flags &= ~bits;
//            }
//            win.setAttributes(winParams);
//        }
//    }
//
//
//    /**
//     * ToolBar+TabLayout状态栏(ToolBar可伸缩)
//     *
//     * @param activity
//     */
//    public static void setToolbarTabLayout(Activity activity, int statusBarColorResId) {
//        if (OsInfoUtil.isLOLLIPOP()) {
//            activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, statusBarColorResId));
//        }
//    }
//
//    /**
//     * DrawerLayout+ToolBar+TabLayout状态栏(ToolBar可伸缩)
//     *
//     * @param activity
//     * @param coordinatorLayout
//     */
//    public static void setDrawerToolbarTabLayout(Activity activity, CoordinatorLayout coordinatorLayout, int statusBarColorResId) {
//        if (OsInfoUtil.isKitkat()) {
//            ViewGroup contentLayout = (ViewGroup) activity.findViewById(android.R.id.content);
//            contentLayout.getChildAt(0).setFitsSystemWindows(false);
//            coordinatorLayout.setFitsSystemWindows(true);
//            setKKStatusBar(activity, statusBarColorResId);
//        }
//    }
//
//    /**
//     * DrawerLayout+ToolBar型状态栏
//     *
//     * @param activity
//     */
//    public static void setDrawerToolbar(Activity activity, int statusBarColorResId) {
//        if (OsInfoUtil.isKitkat()) {
//            ViewGroup contentLayout = (ViewGroup) activity.findViewById(android.R.id.content);
//            contentLayout.getChildAt(0).setFitsSystemWindows(false);
//            setKKStatusBar(activity, statusBarColorResId);
//        }
//    }
//
//    /**
//     * CollapsingToolbarLayout状态栏(可折叠图片)
//     *
//     * @param activity
//     * @param coordinatorLayout
//     * @param appBarLayout
//     * @param imageView
//     * @param toolbar
//     */
//    public static void setCollapsingToolbar(Activity activity, CoordinatorLayout coordinatorLayout,
//                                            AppBarLayout appBarLayout, ImageView imageView, Toolbar toolbar, int statusBarColorResId) {
//        if (OsInfoUtil.isKitkat()) {
//            coordinatorLayout.setFitsSystemWindows(false);
//            appBarLayout.setFitsSystemWindows(false);
//            imageView.setFitsSystemWindows(false);
//            toolbar.setFitsSystemWindows(true);
//            CollapsingToolbarLayout.LayoutParams lp = (CollapsingToolbarLayout.LayoutParams) toolbar.getLayoutParams();
//            lp.height = (int) (AbAppUtil.getStatusBarHeight(activity) +
//                    activity.getResources().getDimension(R.dimen.abc_action_bar_default_height_material));
//            toolbar.setLayoutParams(lp);
//            setCollapsingToolbarStatus(appBarLayout, setKKStatusBar(activity, statusBarColorResId));
//        }
//    }
//
//    /**
//     * Android4.4上CollapsingToolbar折叠时statusBar显示和隐藏
//     *
//     * @param appBarLayout
//     */
//    private static void setCollapsingToolbarStatus(AppBarLayout appBarLayout, final View statusBarView) {
//        ViewCompat.setAlpha(statusBarView, 1);
//        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
//            @Override
//            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//                int maxScroll = appBarLayout.getTotalScrollRange();
//                float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;
//                ViewCompat.setAlpha(statusBarView, percentage);
//            }
//        });
//    }
//
//    private static View setKKStatusBar(Activity activity, int statusBarColorResId) {
//        ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
//        View mStatusBarView = contentView.getChildAt(0);
//        //改变颜色时避免重复添加statusBarView
//        if (mStatusBarView != null && mStatusBarView.getMeasuredHeight() == AbAppUtil.getStatusBarHeight(activity)) {
//            mStatusBarView.setBackgroundColor(ContextCompat.getColor(activity, statusBarColorResId));
//            return mStatusBarView;
//        }
//        mStatusBarView = new View(activity);
//        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                AbAppUtil.getStatusBarHeight(activity));
//        mStatusBarView.setBackgroundColor(ContextCompat.getColor(activity, statusBarColorResId));
//        contentView.addView(mStatusBarView, lp);
//        return mStatusBarView;
//    }
//}
