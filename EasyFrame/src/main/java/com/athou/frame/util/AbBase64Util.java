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

import android.util.Base64;

/**
 * Created by athou on 2016/9/30.
 */
public class AbBase64Util {

    // 加密传入的数据是byte类型的，并非使用decode方法将原始数据转二进制，String类型的数据 使用 str.getBytes()即可
    public static String encode(String str) {
        return encode(str.getBytes());
    }

    public static String encode(byte[] bytes) {
        // 在这里使用的是encode方式，返回的是byte类型加密数据，可使用new String转为String类型
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    // 对base64加密后的数据进行解密
    public static String decode(String strBase64) {
        return new String(Base64.decode(strBase64.getBytes(), Base64.DEFAULT));
    }
}
