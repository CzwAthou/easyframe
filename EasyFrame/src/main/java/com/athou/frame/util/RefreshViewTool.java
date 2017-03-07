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

import android.annotation.TargetApi;
import android.app.Activity;

import com.athou.frame.util.AbStrUtil;
import com.athou.frame.util.SharefUtil;

/**
 * 列表刷新时间工具类
 */
public class RefreshViewTool {
    @TargetApi(17)
    public static String getRefreshTime(Activity context, String name) {
        if (context == null || context.isDestroyed() || AbStrUtil.isEmpty(name)) {
            return "";
        }
        SharefUtil sharefUtil = new SharefUtil(context, "freshTime_" + name);
        String time = sharefUtil.readString("freshTime");
        return time == null ? "" : time;
    }

    @TargetApi(17)
    public static void saveRefreshTime(Activity context, String name, String time) {
        if (context == null || context.isDestroyed() || AbStrUtil.isEmpty(name)) {
            return;
        }
        SharefUtil sharefUtil = new SharefUtil(context, "freshTime_" + name);
        sharefUtil.saveData("freshTime", time);
    }
}
