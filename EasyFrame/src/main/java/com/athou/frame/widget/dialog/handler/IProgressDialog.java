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

package com.athou.frame.widget.dialog.handler;

/**
 * Created by athou on 2016/10/8.
 */

public interface IProgressDialog<T> extends IDialog<T>{

    /**
     * 设置进度条是否是不确定的，这只和创建进度条有关
     * @param indeterminate
     * @return
     */
    T setIndeterminate(boolean indeterminate);

    /**
     * 设置进度条
     * @param total
     * @param progress
     * @return
     */
    T setProgress(long total, long progress);

    /**
     * 设置样式
     * @param style {@link android.app.ProgressDialog#STYLE_SPINNER}  and {@link android.app.ProgressDialog#STYLE_HORIZONTAL}
     * @return
     */
    T setProgressStyle(int style);

    /**
     * 设置进度文本展示 eg:%1d kb/%2d kb
     * @param format
     * @return
     */
    T setProgressNumberFormat(String format);
}
