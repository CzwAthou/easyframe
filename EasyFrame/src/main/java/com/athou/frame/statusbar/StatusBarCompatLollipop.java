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
//import android.annotation.TargetApi;
//import android.app.Activity;
//import android.graphics.Color;
//import android.os.Build;
//import android.support.design.widget.AppBarLayout;
//import android.support.design.widget.CollapsingToolbarLayout;
//import android.support.v4.view.OnApplyWindowInsetsListener;
//import android.support.v4.view.ViewCompat;
//import android.support.v4.view.WindowInsetsCompat;
//import android.support.v7.widget.Toolbar;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.view.WindowManager;
//
///**
// * Created by athou on 2016/10/25.
// */
//@TargetApi(Build.VERSION_CODES.LOLLIPOP)
//public class StatusBarCompatLollipop {
//    /**
//     * set StatusBarColor
//     */
//    public static void setStatusBarColor(Activity activity, int statusColor) {
//        Window window = activity.getWindow();
//
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        window.setStatusBarColor(statusColor);
//        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
//
//        ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
//        View mChildView = mContentView.getChildAt(0);
//        if (mChildView != null) {
//            ViewCompat.setOnApplyWindowInsetsListener(mChildView, new OnApplyWindowInsetsListener() {
//                @Override
//                public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
//                    return insets;
//                }
//            });
//            ViewCompat.setFitsSystemWindows(mChildView, true);
//            ViewCompat.requestApplyInsets(mChildView);
//        }
//    }
//
//    /**
//     * translucentStatusBar(full-screen)
//     *
//     * @param hideStatusBarBackground hide statusBar's shadow
//     */
//    public static void translucentStatusBar(Activity activity, boolean hideStatusBarBackground) {
//        Window window = activity.getWindow();
//
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        if (hideStatusBarBackground) {
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.setStatusBarColor(Color.TRANSPARENT);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//        } else {
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
//        }
//
//        ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
//        View mChildView = mContentView.getChildAt(0);
//        if (mChildView != null) {
//            ViewCompat.setOnApplyWindowInsetsListener(mChildView, new OnApplyWindowInsetsListener() {
//                @Override
//                public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
//                    return insets;
//                }
//            });
//            ViewCompat.setFitsSystemWindows(mChildView, false);
//            ViewCompat.requestApplyInsets(mChildView);
//
//        }
//    }
//
//    /**
//     * compat for CollapsingToolbarLayout
//     */
//    public static void setStatusBarColorForCollapsingToolbar(Activity activity, final AppBarLayout appBarLayout, CollapsingToolbarLayout collapsingToolbarLayout,
//                                                             Toolbar toolbar, int statusColor) {
//        Window window = activity.getWindow();
//
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        window.setStatusBarColor(Color.TRANSPARENT);
//        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
//
//        ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
//        View mChildView = mContentView.getChildAt(0);
//        if (mChildView != null) {
//            ViewCompat.setOnApplyWindowInsetsListener(mChildView, new OnApplyWindowInsetsListener() {
//                @Override
//                public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
//                    return insets;
//                }
//            });
//            ViewCompat.setFitsSystemWindows(mChildView, true);
//            ViewCompat.requestApplyInsets(mChildView);
//        }
//
//        ((View) appBarLayout.getParent()).setFitsSystemWindows(true);
//        appBarLayout.setFitsSystemWindows(true);
//        collapsingToolbarLayout.setFitsSystemWindows(true);
//        collapsingToolbarLayout.getChildAt(0).setFitsSystemWindows(true);
//        toolbar.setFitsSystemWindows(false);
//
//        collapsingToolbarLayout.setStatusBarScrimColor(statusColor);
//    }
//}
