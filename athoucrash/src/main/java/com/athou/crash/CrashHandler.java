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
package com.athou.crash;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.athou.crash.path.PagePath;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 捕获全局异常类
 *
 * @author athou
 */
public class CrashHandler implements UncaughtExceptionHandler {
    private String TAG = CrashHandler.class.getSimpleName();

    public String ERROR_LOG_NAME = null;
    public String ERROR_LOG_PATH = null;

    private static CrashHandler INSTANCE;
    private UncaughtExceptionHandler mDefaultHandler;
    private ICrashHandler myApplication;

    private Map<String, String> infos = new HashMap<String, String>();

    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        synchronized (CrashHandler.class) {
            if (INSTANCE == null) {
                INSTANCE = new CrashHandler();
            }
            return INSTANCE;
        }
    }

    public void init(ICrashHandler application) {
        if (application instanceof Application) {
            this.myApplication = application;
            initLogoFile();
            mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
            Thread.setDefaultUncaughtExceptionHandler(this);
        } else {
            throw new RuntimeException("you must implements ICrashHandler in your application");
        }
    }

    private void initLogoFile() {
        ERROR_LOG_NAME = "error.txt";
        ERROR_LOG_PATH = Environment.getExternalStorageDirectory() + File.separator
                + ((Application) myApplication).getPackageName() + "/log/crash/";
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(500);
            } catch (Exception e) {
            }
            String name = thread.getName();
            if ("main".equals(name)) { //主线程奔溃，回调给用户，以便重启应用
                if (myApplication != null) {
                    myApplication.OnMainThreadCrash();
                }
                postError(); // 非主线程奔溃/主线程奔溃，都会提交错误信息
            } else {
                new Handler(Looper.getMainLooper())
                        .post(new Runnable() {
                            @Override
                            public void run() {
                                postError(); // 非主线程奔溃/主线程奔溃，都会提交错误信息
                            }
                        });
            }
        }
    }

    /**
     * 处理奔溃信息
     */
    private boolean handleException(final Throwable ex) {
        if (ex == null) {
            return false;
        }
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                collectDeviceInfo();
                saveCrashInfo2File(ex);
                Looper.loop();
            }
        }.start();
        return true;
    }

    /**
     * 收集终端信息
     */
    private void collectDeviceInfo() {
        infos.put("androidSDK", String.valueOf(Build.VERSION.SDK_INT));
        infos.put("crashTime", String.valueOf(new Date().getTime()));
        Map<String, String> data = myApplication.requestData();
        if (data != null && !data.isEmpty()) {
            for (String key : data.keySet()) {
                infos.put(key, data.get(key));
            }
        }
        try {
            Application ctx = (Application) myApplication;
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                infos.put("versionName", pi.versionName == null ? "null" : pi.versionName);
                infos.put("versionCode", pi.versionCode + "");
            }
        } catch (NameNotFoundException e) {
            Log.e(TAG, "an error occured when collect package info:" + e.getMessage());
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
            } catch (Exception e) {
                Log.e(TAG, "an error occured when collect crash info:" + e.getMessage());
            }
        }
    }

    /**
     * 保存奔溃信息到文件中
     */
    private void saveCrashInfo2File(Throwable ex) {
        File file = getErrorFile();
        if (file == null) {
            return;
        }
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet()) { //保存设备信息
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\r\n");
        }
        sb.append("操作步骤:\n" + PagePath.getPathData() + "\n"); //保存操作步骤
        sb.append("[err]");
        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        ex.printStackTrace(pw);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(pw);
            cause = cause.getCause();
        }
        pw.close();
        String result = writer.toString();
        sb.append("异常原因:" + ex.getClass().getSimpleName() + ":" + ex.getMessage() + "\n");
        sb.append("异常详细:\n");
        sb.append(result);
        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(sb.toString().getBytes());
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取奔溃日志文件
     */
    public File getErrorFile() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            try {
                File file = new File(ERROR_LOG_PATH, ERROR_LOG_NAME);
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                if (!file.exists()) {
                    file.createNewFile();
                }
                return file;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            return null;
        }
    }

    public String getErrorLogString() {
        return getErrorLogString(getErrorFile());
    }

    public String getErrorLogString(File logFile) {
        String content = null;
        FileInputStream fis = null;
        try {
            if (logFile != null && logFile.exists()) {
                byte[] data = new byte[(int) logFile.length()];
                fis = new FileInputStream(logFile);
                fis.read(data);
                content = new String(data);
                data = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return content;
    }

    /**
     * 上传日志
     */
    public void postError() {
        new AsyncTask<Void, String, String>() {
            @Override
            protected String doInBackground(Void... params) {
                return getErrorLogString(getErrorFile());
            }

            @Override
            protected void onPostExecute(String err) {
                super.onPostExecute(err);
                if (TextUtils.isEmpty(err)) {
                    return;
                }
                if (myApplication != null) {
                    myApplication.postCrashLog(err);
                }
            }
        }.execute();
    }
}
