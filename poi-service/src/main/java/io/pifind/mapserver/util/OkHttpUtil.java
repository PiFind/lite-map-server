package io.pifind.mapserver.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import okhttp3.*;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author shiwei.zhou
 * @Description:
 * @date 2023/3/179:48 上午
 */
public class OkHttpUtil {

	private static OkHttpClient client;
	//线上
	public static final MediaType JSONs = MediaType.parse("application/json; charset=utf-8");

	static {
		OkHttpClient.Builder builder = new OkHttpClient.Builder();
		builder.connectTimeout(10000L, TimeUnit.SECONDS);
		builder.sslSocketFactory(SSLSocketClient.getSSLSocketFactory(), SSLSocketClient.getX509TrustManager());
		builder.hostnameVerifier(new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		});
		client = builder.build();
	}

	public static Response doGet(String url, String authorization) throws IOException {
		Request request = new Request.Builder().url(url).addHeader("Authorization", authorization).build();
		Response response = client.newCall(request).execute();
		return response;
	}

	public static Response doGet(String url) throws IOException {
		Request request = new Request.Builder().url(url).build();
		Response response = client.newCall(request).execute();
		return response;
	}

	public static <T> T doGet(String url, Class<T> cls) throws IOException {
		Response response = doGet(url);
		return JSONObject.parseObject(response.body().string(), cls);
	}

	public static Response doPostJson(String url, Map<String, Object> params) throws IOException {
		Gson gson = new Gson();
		if (params == null) {
			params = Maps.newHashMap();
		}
		String jsonParam = gson.toJson(params);
		RequestBody body = RequestBody.create(JSONs, jsonParam);
		Request request = new Request.Builder().url(url).post(body).build();
		Response response = client.newCall(request).execute();
		return response;
	}

	public <T> T doPostJson(String url, Map<String, Object> params, Class<T> cls) throws IOException {
		Response response = doPostJson(url, params);
		return JSONObject.parseObject(response.body().string(), cls);
	}

	public static <T> T doPostJson(String url, Object data, Map<String, String> header, Class<T> cls) throws IOException {
		Response response = doPostJson(url, data, header);
		return JSONObject.parseObject(response.body().string(), cls);
	}

	public static <T> T doPostJson(String url, Object data, Class<T> cls) throws IOException {
		Response response = doPostJson(url, data, new HashMap<>());
		String body = response.body().string();
		return JSONObject.parseObject(body, cls);
	}

	public static Response doPostJson(String url, Object data, Map<String, String> header) throws IOException {
		String content = JSON.toJSONString(data);
		Headers headers = Headers.of(header);
		RequestBody body = RequestBody.create(JSONs, content);
		Request request = new Request.Builder().url(url).headers(headers).post(body).build();
		return client.newCall(request).execute();
	}

	public static <T> T doPostJson(String url, Object data, Map<String, String> header, TypeReference<T> type) throws IOException {
		Response response = doPostJson(url, data, header);
		return JSONObject.parseObject(response.body().string(), type);
	}
}
