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

package com.athou.frame;

import android.app.Application;

import com.athou.crash.CrashHandler;
import com.athou.crash.ICrashHandler;
import com.athou.frame.constants.AbAppData;
import com.athou.frame.monitor.ForegroundCallbacks;
import com.athou.frame.util.AbAppUtil;
import com.athou.frame.util.L;
import com.athou.frame.util.SharefUtil;
import com.athou.frame.util.T;

/**
 * application基类
 *
 * @author cai
 */
public abstract class FinalApplication extends Application implements ICrashHandler {
    //奔溃日志收集类
    private CrashHandler crashHandler = null;
    //是否应该奔溃后重启APP
    private boolean restartAfterCrash = false;
    //配置文件
    private static SharefUtil mSharf = null;
    //app单例
    public static FinalApplication unique = null;
    //是否app处于前台
    public static volatile boolean isForeground = false;

    @Override
    public void onCreate() {
        super.onCreate();
        AbAppData.DEBUG = AbAppUtil.isDebug(this); //初始化日志
        catchError();

        ForegroundCallbacks.getInstance().attach(this);
        ForegroundCallbacks.getInstance().addListener(new ForegroundCallbacks.Listener() {
            @Override
            public void onBecameForeground() {
                L.d("当前程序切换到前台");
                isForeground = true;
            }

            @Override
            public void onBecameBackground() {
                L.d("当前程序切换到后台");
                isForeground = false;
            }
        });
    }

    /**
     * 设置捕获全局异常<br>
     * <p>
     * application必须实现{@link ICrashHandler}
     */
    private void catchError() {
        if (AbAppData.DEBUG) {
            return;
        }
        crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);
    }

    public CrashHandler getCrashHandler() {
        return crashHandler;
    }

    /**
     * 是否奔溃后重启APP
     *
     * @return
     */
    public boolean isRestartAfterCrash() {
        return (!AbAppData.DEBUG) && restartAfterCrash;
    }

    public void setRestartAfterCrash(boolean restartAfterCrash) {
        this.restartAfterCrash = restartAfterCrash;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ForegroundCallbacks.getInstance().detach(this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        T.show(this, "系统内存不足!");
        System.gc();
    }

    /**
     * 获取本地配置文件（“app_config”）
     */
    public final synchronized SharefUtil getSharefUtil() {
        if (mSharf == null)
            mSharf = new SharefUtil(this, "app_config");
        return mSharf;
    }

    /**
     * 获取版本号
     *
     * @return
     */
    public int getVersionCode() {
        return AbAppUtil.getVersionCode(this);
    }

    /**
     * 获取版本名称
     */
    public String getVersionName() {
        return AbAppUtil.getVersionName(this);
    }

    /**
     * 获取程序 图标
     */
    public int getAppIconId() {
        return AbAppUtil.getAppIconId(this);
    }

    /**
     * 获取程序的名字
     */
    public String getAppName() {
        return AbAppUtil.getAppName(this);
    }
}
