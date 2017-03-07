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

package com.athou.frame.helper;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import com.athou.frame.util.L;

import java.util.Stack;

/**
 * activity管理类
 */
public class ScreenManager {

    public static int screenWidth;
    public static int screenHeight;

    private static Stack<Activity> activityStack;
    private static ScreenManager instance;

    private ScreenManager() {
        activityStack = new Stack<Activity>();
    }

    public static ScreenManager getInstance() {
        synchronized (ScreenManager.class) {
            if (instance == null) {
                instance = new ScreenManager();
            }
            return instance;
        }
    }

    /**
     * 获取指定的Activity
     */
    public static Activity getActivity(Class<?> cls) {
        if (activityStack != null)
            for (Activity activity : activityStack) {
                if (activity.getClass().equals(cls)) {
                    return activity;
                }
            }
        return null;
    }

    /**
     * 获取当前activity
     *
     * @return
     */
    public Activity currentActivity() {
        Activity activity = null;
        if (!activityStack.isEmpty()) {
            activity = activityStack.lastElement();
        }
        return activity;
    }

    /**
     * 添加activity
     *
     * @param activity
     */
    public void pushActivity(Activity activity) {
        L.i("当前入栈activity ===>>>" + activity.getClass().getSimpleName());
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        if (activityStack.contains(activity)) {
            activityStack.remove(activity);
        }
        activityStack.add(activity);
    }

    /**
     * 销毁栈顶的activity
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        if (activity != null) {
            activity.finish();
            activity = null;
        }
    }

    /**
     * 销毁指定的activity
     *
     * @param activityClass
     */
    public void finishActivity(Class<? extends Activity> activityClass) {
        for (int i = 0; i < activityStack.size(); i++) {
            Activity activity = activityStack.elementAt(i);
            if (activity != null && activity.getClass().equals(activityClass)) {
                finishActivity(activity);
                break;
            }
        }
    }

    /**
     * 销毁指定的activity
     *
     * @param activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 销毁所有activity
     */
    public void finishAllActivity() {
        while (true) {
            if (activityStack == null || activityStack.isEmpty()){
                return;
            }
            Activity activity = activityStack.lastElement();
            finishActivity(activity);
            if (activityStack.isEmpty()) {
                return;
            }
        }
    }

    /**
     * 移除activity, 但是不销毁
     *
     * @param activity
     */
    public void removeActivity(Activity activity) {
        L.i("当前出栈activity ===>>>" + activity.getClass().getSimpleName());
        if (activity != null && activityStack.contains(activity)) {
            activityStack.remove(activity);
            activity = null;
        }
    }

    /**
     * 销毁所有activity，保留最开始的activity
     */
    public void popAllActivityKeepTop() {
        while (true) {
            if (size() <= 1) {
                break;
            }
            Activity activity = currentActivity();
            if (activity == null) {
                break;
            }
            finishActivity(activity);
        }
    }

    /**
     * 销毁所有activity，保留指定activity
     *
     * @param cls
     */
    public void popAllActivityExceptOne(Class<? extends Activity> cls) {
        if (activityStack.isEmpty()) {
            return;
        }
        int i = 0;
        while (true) {
            int size = activityStack.size();
            if (i >= size) {
                return;
            }
            Activity activity = activityStack.get(i);
            i++;
            if (activity == null) {
                continue;
            }
            if (activity.getClass().equals(cls)) {
                continue;
            }
            finishActivity(activity);
            i--;
        }
    }

    public void quitApp(Context context) {
        try {
            finishAllActivity();
        } catch (Exception e) {
        }

        // 获取packagemanager的实例
        try {
            ActivityManager activityMgr = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.killBackgroundProcesses(context.getPackageName());
            activityStack = null;
            instance = null;
        } catch (Exception e) {
        } finally {
            System.exit(0);
        }
    }

    /**
     * 是否有这个activity（这个activity是否在activity栈内）
     *
     * @param cls
     * @return
     */
    public boolean hasActivity(Class<? extends Activity> cls) {
        for (int i = 0; i < activityStack.size(); i++) {
            if (activityStack.get(i) != null && activityStack.get(i).getClass().equals(cls)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取activity个数
     *
     * @return
     */
    public int size() {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        return activityStack.size();
    }
}
