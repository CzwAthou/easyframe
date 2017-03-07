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

package com.athou.crash.path;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.athou.crash.annotation.PageName;

import java.lang.annotation.Annotation;

/**
 * Created by athou on 2016/10/10.
 */
public class PagePath {
    private static StringBuffer pathData = null;

    public static void onCreate(Activity c) {
        if (pathData == null) {
            pathData = new StringBuffer();
        }
        pathData.append("[Activity Start]" + getPageName(c) + " ==> ");
    }

    public static void onResume(Activity c) {
        if (pathData == null) {
            pathData = new StringBuffer();
        }
        pathData.append("[Activity onResume]" + getPageName(c) + " ==> ");
    }

    public static void onPause(Activity c) {
        if (pathData == null) {
            pathData = new StringBuffer();
        }
        pathData.append("[Activity onPause]" + getPageName(c) + " ==> ");
    }

    public static void onDestroy(Activity c) {
        if (pathData == null) {
            pathData = new StringBuffer();
        }
        pathData.append("[Activity Exit]" + getPageName(c) + " ==> ");
    }

    public static void onCreate(Fragment c) {
        if (pathData == null) {
            pathData = new StringBuffer();
        }
        pathData.append("[Fragment Start]" + getPageName(c) + " ==> ");
    }

    public static void onResume(Fragment c) {
        if (pathData == null) {
            pathData = new StringBuffer();
        }
        pathData.append("[Fragment onResume]" + getPageName(c) + " ==> ");
    }

    public static void onPause(Fragment c) {
        if (pathData == null) {
            pathData = new StringBuffer();
        }
        pathData.append("[Fragment onPause]" + getPageName(c) + " ==> ");
    }

    public static void onDestroy(Fragment c) {
        if (pathData == null) {
            pathData = new StringBuffer();
        }
        pathData.append("[Fragment onDestroy]" + getPageName(c) + " ==> ");
    }

    public static String getPathData() {
        String result = "";
        if (pathData != null) {
            result = pathData.toString();
        }
        return result;
    }

    public static String getPageName(Activity c) {
        Annotation[] annotations = c.getClass().getAnnotations();
        PageName pageName = null;
        for (Annotation annotation : annotations) {
            if (annotation instanceof PageName) {
                pageName = (PageName) annotation;
                return "[Activity]" + pageName.name();
            }
        }
        return c.getClass().getSimpleName();
    }

    public static String getPageName(Fragment c) {
        Annotation[] annotations = c.getClass().getAnnotations();
        PageName pageName = null;
        for (Annotation annotation : annotations) {
            if (annotation instanceof PageName) {
                pageName = (PageName) annotation;
                return "[Activity]" + pageName.name();
            }
        }
        return c.getClass().getSimpleName();
    }
}
