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

package com.athou.frame.net.util;

import com.athou.frame.util.L;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * ping 工具类
 * 
 * @author athou
 * 
 */
public class PingUtil {

	public static boolean pingIpAddr(String ip) {
		try {
			Process process = Runtime.getRuntime().exec("su");
			DataOutputStream os = new DataOutputStream(process.getOutputStream());
			os.writeBytes("/system/bin/ping -c 1 -w 100 " + ip + "\n");
			os.flush();
			process.getErrorStream().close();
			InputStream in = process.getInputStream();
			String result = "";
			byte[] re = new byte[1024];
			while (in.read(re) != -1) {
				result = result + new String(re);
			}
			in.close();
			int code = process.waitFor();
			L.d("result = " + result + " code = " + code);
			if (code == 0) {
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return false;
	}
}
