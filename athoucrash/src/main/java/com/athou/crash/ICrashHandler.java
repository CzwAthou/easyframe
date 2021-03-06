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

import java.io.File;
import java.util.Map;

/**
 * Created by athou on 2016/10/10.
 */

public interface ICrashHandler {

    /**
     * 主线程crash了，you can do something here
     */
    void OnMainThreadCrash();

    /**
     *  收集用户自定义的信息
     */
    Map<String, String> requestData();

    /**
     * post the err log into your server
     * @param err
     */
    void postCrashLog(String err);
}
