package util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

public class HttpUtil {

	private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

	public static JSONObject postParam(String url, Pair<String, String>... params) {
		// String param = "";
		// for (Pair<String, String> s : params) {
		// if (param != "") {
		// param += "&";
		// }
		// param += s.getLeft() + "='" + s.getRight() + "'";
		// }
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print("{\"game\":17,\"channel\":-1,\"pageIndex\":1,\"pageSize\":100}");
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			JSONObject obj = new JSONObject();
			obj.put("code", 201);
			obj.put("msg", "系统异常，效验短信失败");
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return JSONObject.parseObject(result);
	}

	private static final int TIME_OUT = 5;

	public static String sendGet(String httpUrl, Map<String, String> parameter) {
		if (parameter == null || httpUrl == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		Iterator<Map.Entry<String, String>> iterator = parameter.entrySet().iterator();
		while (iterator.hasNext()) {
			if (sb.length() > 0) {
				sb.append('&');
			}
			Entry<String, String> entry = iterator.next();
			String key = entry.getKey();
			String value;
			try {
				value = URLEncoder.encode(entry.getValue(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				value = "";
			}
			sb.append(key).append('=').append(value);
		}
		String urlStr = null;
		if (httpUrl.lastIndexOf('?') != -1) {
			urlStr = httpUrl + '&' + sb.toString();
		} else {
			urlStr = httpUrl + '?' + sb.toString();
		}

		logger.info("sendGet:{}", urlStr);

		HttpURLConnection httpCon = null;
		String responseBody = null;
		try {
			URL url = new URL(urlStr);
			httpCon = (HttpURLConnection) url.openConnection();
			httpCon.setDoOutput(true);
			httpCon.setRequestMethod("GET");
			httpCon.setConnectTimeout(TIME_OUT * 1000);
			httpCon.setReadTimeout(TIME_OUT * 1000);

			// 开始读取返回的内容
			InputStream in = httpCon.getInputStream();
			byte[] readByte = new byte[1024];
			// 读取返回的内容
			int readCount = in.read(readByte, 0, 1024);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while (readCount != -1) {
				baos.write(readByte, 0, readCount);
				readCount = in.read(readByte, 0, 1024);
			}
			responseBody = new String(baos.toByteArray(), "UTF-8");
			baos.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("sendGet exception");
		} finally {
			if (httpCon != null)
				httpCon.disconnect();
		}
		return responseBody;
	}

	public static String sendGet(String httpUrl, String cookie) {
		if (httpUrl == null) {
			return null;
		}

		HttpURLConnection httpCon = null;
		String responseBody = null;
		try {
			URL url = new URL(httpUrl);
			httpCon = (HttpURLConnection) url.openConnection();
			httpCon.setDoOutput(true);
			httpCon.setRequestMethod("GET");
			httpCon.setConnectTimeout(TIME_OUT * 1000);
			httpCon.setReadTimeout(TIME_OUT * 1000);

			httpCon.setRequestProperty("Cookie", cookie);

			// 开始读取返回的内容
			InputStream in = httpCon.getInputStream();
			byte[] readByte = new byte[1024];
			// 读取返回的内容
			int readCount = in.read(readByte, 0, 1024);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while (readCount != -1) {
				baos.write(readByte, 0, readCount);
				readCount = in.read(readByte, 0, 1024);
			}
			responseBody = new String(baos.toByteArray(), "UTF-8");
			baos.close();
		} catch (Exception e) {
			LogHelper.ERROR.info(e.getMessage(), e);
		} finally {
			if (httpCon != null)
				httpCon.disconnect();
		}
		return responseBody;
	}

	public static String doGet(String urlPath, String cookie) {
		try {
			URL url = new URL(urlPath);
			URLConnection conn = url.openConnection();
			conn.addRequestProperty("Method", "GET");
			if (cookie != null) {
				conn.addRequestProperty("Cookie", cookie);
			}
			conn.setDoInput(true);
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			System.out.println("请求响应结果：" + sb);
			return sb.toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getParam(Map<String, String> param) {
		StringBuilder str = new StringBuilder();
		int size = 0;
		for (Map.Entry<String, String> m : param.entrySet()) {
			str.append(m.getKey());
			str.append("=");
			str.append(m.getValue());
			if (size < param.size() - 1) {
				str.append("&");
			}
			size++;
		}
		System.out.println(str.toString());
		return str.toString();
	}

	//
	// /**
	// * 获取post请求
	// *
	// * @param request
	// * @param string
	// * @param param
	// * @return
	// */
	// public static byte[] doPost(HttpServletRequest request, String url,
	// Map<String, String> param) throws Exception {
	// // 当传入的url返回不为空的时候，读取数据
	// InputStream input = null;
	// PrintWriter out = null;
	// byte[] data = null;// 提高字符数据的生成
	// if (StringUtils.isNotBlank(url)) {
	// try {
	// // 设置请求的头信息
	// URL urlInfo = new URL(url);
	// URLConnection connection = urlInfo.openConnection();
	// connection.addRequestProperty("Host", urlInfo.getHost());// 设置头信息
	// connection.addRequestProperty("Connection", "keep-alive");
	// connection.addRequestProperty("Accept", "*/*");
	// connection.addRequestProperty("Cookie", getSessionIncookie(request));//
	// 设置获取的cookie
	// connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1;
	// Win64; x64)");
	// // 发送POST请求必须设置如下两行
	// connection.setDoOutput(true);
	// connection.setDoInput(true);
	// // 获取URLConnection对象对应的输出流
	// out = new PrintWriter(connection.getOutputStream());
	// // 发送请求参数
	// out.print(getParam(param));
	// // flush输出流的缓冲
	// out.flush();
	// // 获取所有响应头字段
	// getCookieToSession(request, connection);
	// // 获取请求回来的信息
	// input = connection.getInputStream();// 定义返回数据的格式
	// data = new byte[input.available()];
	// input.read(data);
	//
	// } catch (Exception e) {
	// throw new Exception("读取Url数据失败：" + url, e);
	// } finally {
	// try {
	// input.close();
	// } catch (Exception e) {
	// }
	// }
	// }
	// return data;
	// }

	public static void main(String[] args) {
		Map<String, Integer> map = new HashMap<>();
		map.put("playerId", 2284);
		Gson gson = new Gson();
		String s = gson.toJson(map);
		System.out.println(s);

		String result = postParam("http://127.0.0.1:9081/template/gamePlayers", new Pair<>("playerId", "2284")).toString();
		System.err.println(result);
	}
}
