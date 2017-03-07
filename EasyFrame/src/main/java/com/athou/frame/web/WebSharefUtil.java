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

package com.athou.frame.web;

import android.content.Context;
import android.content.SharedPreferences;

public class WebSharefUtil {
	private SharedPreferences sp;
	private SharedPreferences.Editor editor;
	private String fileName;

	public WebSharefUtil(Context context, String fileName) {
		this.fileName = fileName;
		this.sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		this.editor = sp.edit();
	}

	// 是否自适应屏幕
	public boolean getAutoFitScreen() {
		return sp.getBoolean("autofitscreen", false);
	}

	public void setAutoFitScreen(boolean autoFit) {
		editor.putBoolean("autofitscreen", autoFit);
		editor.commit();
	}
}
