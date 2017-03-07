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

package com.athou.frame.monitor;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;

import com.athou.frame.util.L;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 监听app状态
 * Created by athou on 2017/1/24.
 */
public class ForegroundCallbacks implements Application.ActivityLifecycleCallbacks {

    public static final long CHECK_DELAY = 500;
    private static ForegroundCallbacks instance;
    private boolean foreground = false, paused = true;
    private Handler handler = new Handler();
    private List<Listener> listeners = null;
    private Runnable check;

    public ForegroundCallbacks() {
        listeners = new CopyOnWriteArrayList<Listener>();
    }

    public static ForegroundCallbacks getInstance() {
        if (instance == null) {
            instance = new ForegroundCallbacks();
        }
        return instance;
    }

    public void attach(Application application) {
        application.registerActivityLifecycleCallbacks(this);
    }

    public void detach(Application application) {
        application.unregisterActivityLifecycleCallbacks(this);

        listeners.clear();
    }

    public boolean isForeground() {
        return foreground;
    }

    public boolean isBackground() {
        return !foreground;
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    @Override
    public void onActivityResumed(Activity activity) {
        paused = false;
        boolean wasBackground = !foreground;
        foreground = true;
        if (check != null)
            handler.removeCallbacks(check);
        if (wasBackground) {
            L.d("went foreground");

            for (Listener l : listeners) {
                try {
                    l.onBecameForeground();
                } catch (Exception exc) {
                    L.d("Listener threw exception!:" + exc.toString());
                }
            }
        } else {
            L.d("still foreground");
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        paused = true;
        if (check != null)
            handler.removeCallbacks(check);
        handler.postDelayed(check = new Runnable() {
            @Override
            public void run() {
                if (foreground && paused) {
                    foreground = false;
                    L.d("went background");
                    for (Listener l : listeners) {
                        try {
                            l.onBecameBackground();
                        } catch (Exception exc) {
                            L.d("Listener threw exception!:" + exc.toString());
                        }
                    }
                } else {
                    L.d("still foreground");
                }
            }
        }, CHECK_DELAY);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }

    public interface Listener {
        void onBecameForeground();

        void onBecameBackground();
    }
}
