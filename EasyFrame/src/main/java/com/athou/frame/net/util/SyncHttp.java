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

import com.athou.frame.util.ConvertUtil;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class SyncHttp {

	public static final int HTTP_OK = 200;
	public static final int HTTP_CLIENT = 400;
	public static final int HTTP_SERVER = 500;

	protected static final int TIME_OUT = 1000 * 20; // 超时时间
	protected static final String METHOD_POST = "POST";
	protected static final String METHOD_GET = "GET";
	protected static final String CHARSET = "UTF-8"; // 编码
	
	public static HttpURLConnection httpGetConn(String path) throws IOException {
		URL url = new URL(path);
		HttpURLConnection conn = null;
		conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod(METHOD_GET);
		conn.setConnectTimeout(TIME_OUT);
		conn.setDoInput(true);
		conn.connect();
		return conn;
	}

	public static InputStream httpGet(String path) throws IOException {
		URL url = new URL(path);
		HttpURLConnection conn = null;
		conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod(METHOD_GET);
		conn.setConnectTimeout(TIME_OUT);
		conn.setDoInput(true);
		conn.connect();
		int responseCode = conn.getResponseCode();
		InputStream is = null;
		if (responseCode == HTTP_OK) {
			is = conn.getInputStream();
		} else if (responseCode == HTTP_CLIENT) {
			String result = "<Programe result=\"false\" message=\"客户端错误\" />";
			is = new ByteArrayInputStream(result.getBytes());
		} else if (responseCode == HTTP_SERVER) {
			String result = "<Programe result=\"false\" message=\"服务器错误\" />";
			is = new ByteArrayInputStream(result.getBytes());
		} else {
			String result = "<Programe result=\"false\" message=\"未知错误\" />";
			is = new ByteArrayInputStream(result.getBytes());
		}
		if (conn != null) {
			conn = null;
		}
		return is;
	}

	public static String httpGetStr(String path) throws IOException {
		URL url = new URL(path);

		HttpURLConnection conn = null;

		conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod(METHOD_GET);
		conn.setConnectTimeout(TIME_OUT);
		conn.setDoInput(true);
		conn.connect();
		int responseCode = conn.getResponseCode();
		String response = null;
		if (responseCode == HTTP_OK) {
			InputStream is = conn.getInputStream();
			response = ConvertUtil.isToString(is);
		} else if (responseCode == 400) {
			response = "<Programe result=\"false\" message=\"客户端错误\" />";
		} else if (responseCode == 500) {
			response = "<Programe result=\"false\" message=\"服务器错误\" />";
		} else {
			response = "<Programe result=\"false\" message=\"未知错误\" />";
		}
		return response;
	}

	public static String httpPost(String path, List<NameValuePair> params) {
		String res = "";
		org.apache.http.client.HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(path);
		try {
			post.setEntity(new UrlEncodedFormEntity(params, CHARSET));
			HttpResponse response = client.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {// ���������ݳɹ�
				HttpEntity entity = response.getEntity();
				InputStream is = entity.getContent();
				DataInputStream dis = new DataInputStream(is);
				res = dis.readUTF();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}

	public static String httpPost(String path, String name, String val) {
		Map<String, String> parmas = new HashMap<String, String>();
		parmas.put(name, val);
		DefaultHttpClient client = new DefaultHttpClient();// http�ͻ���
		HttpPost httpPost = new HttpPost();
		ArrayList<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
		if (parmas != null) {
			Set<String> keys = parmas.keySet();
			for (Iterator<String> i = keys.iterator(); i.hasNext();) {
				String key = (String) i.next();
				pairs.add(new BasicNameValuePair(key, parmas.get(key)));
			}
		}
		InputStream content = null;
		String result = null;
		try {
			UrlEncodedFormEntity p_entity = new UrlEncodedFormEntity(pairs, CHARSET);
			httpPost.setEntity(p_entity);
			HttpResponse response = client.execute(httpPost);
			HttpEntity entity = response.getEntity();
			content = entity.getContent();
			result = ConvertUtil.isToString(content);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String httpPost(String urlStr, String paramsStr) throws Exception {

		byte[] data = paramsStr.getBytes();
		URL url = null;
		HttpURLConnection conn = null;
		InputStream inStream = null;
		String response = null;
		try {
			url = new URL(urlStr);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(TIME_OUT);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod(METHOD_POST);
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Charset", CHARSET);
			conn.setRequestProperty("Content-Length", String.valueOf(data.length));
			conn.setRequestProperty("Content-ServiceType", "application/x-www-form-urlencoded");
			conn.connect();
			DataOutputStream outputStream = new DataOutputStream(conn.getOutputStream());
			outputStream.write(data);
			outputStream.flush();
			outputStream.close();
			int responseCode = conn.getResponseCode();
			if (responseCode == HTTP_OK) {
				inStream = conn.getInputStream();
				response = ConvertUtil.isToString(inStream);
			} else {
				response = "错误代码:" + responseCode;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			conn.disconnect();
		}
		return response;
	}

}
