package http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import actor.ICallback;

/**
 * Created by Administrator on 2016/12/7.
 */
public class HttpUtil {

	private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

	private static ThreadLocal<HttpClient> httpClients = new ThreadLocal<>();

	public static HttpClient getHttpClient() {
		HttpClient client = httpClients.get();
		if (client != null) {
			return client;
		}
		client = new DefaultHttpClient();
		client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);
		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 3000);
		httpClients.set(client);
		return client;
	}

	private static String returnResultString(HttpEntity resEntity) {
		try {
			String result = EntityUtils.toString(resEntity, "utf-8");
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			return "failed";
		}
	}

	public static String sendPost(String url, String content) {
		HttpClient httpclient = getHttpClient();
		HttpPost httpost = new HttpPost(url);
		httpost.setEntity(new StringEntity(content, "utf-8"));
		HttpResponse response;
		try {
			response = httpclient.execute(httpost);
		} catch (Exception e) {
//			e.printStackTrace();
			return "error";
		}
		HttpEntity entity = response.getEntity();
		String charset = EntityUtils.getContentCharSet(entity);
		String body = null;
		try {
			body = EntityUtils.toString(entity, "utf-8");
		} catch (Exception e) {
//			e.printStackTrace();
			return "error";
		}
		return body;
	}

	public static String sendPost(String url, Map<String, String> params) {
		HttpClient httpclient = getHttpClient();
		HttpPost httpost = new HttpPost(url);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		Set<String> keySet = params.keySet();
		for (String key : keySet) {
			nvps.add(new BasicNameValuePair(key, params.get(key)));
		}
		try {
			httpost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		HttpResponse response = null;
		try {
			response = httpclient.execute(httpost);
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
		HttpEntity entity = response.getEntity();
		String charset = EntityUtils.getContentCharSet(entity);
		String body = null;
		try {
			body = EntityUtils.toString(entity, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
		return body;
	}

	public static void sendPost(String url, Map<String, String> params, ICallback callback) {
		logger.info(" the post url is {}", url);
		HttpClient httpclient = getHttpClient();
		HttpPost httpost = new HttpPost(url);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		Set<String> keySet = params.keySet();
		for (String key : keySet) {
			nvps.add(new BasicNameValuePair(key, params.get(key)));
		}
		try {
			httpost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		HttpResponse response = null;
		try {
			response = httpclient.execute(httpost);
		} catch (Exception e) {
			e.printStackTrace();
			if (callback != null) {
				callback.onResult(e);
			}
			return;
		}
		HttpEntity entity = response.getEntity();
		String charset = EntityUtils.getContentCharSet(entity);
		String body = null;
		try {
			body = EntityUtils.toString(entity, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
			if (callback != null) {
				callback.onResult(e);
			}
			return;
		}
		if (callback != null) {
			callback.onResult(body);
		}
	}

	public static void main(String[] args) {
		double tmp1 = 134391.0;
		long tmp2 = Math.round(134391 / 2.0f);
		System.out.println("tmp2:" + tmp2 + "  :" + tmp2 * 2);
	}


	public static String sendGet(String url, String queryString) {
		return null;
	}

	public static String sendGet(String url) {
		HttpClient httpClient = getHttpClient();
		HttpGet httpGet = new HttpGet(url);
		HttpResponse response = null;
//		try {
//			response = httpClient.execute(httpGet);
//		} catch (IOException e) {
//			logger.error("", e);
			return "failed";
//		}
//		int statusCode = response.getStatusLine().getStatusCode();
////		logger.info("the status code is {}  url {}", statusCode, url);
//		HttpEntity resEntity = response.getEntity();
//		return returnResultString(resEntity);
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 *
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPostEx(String url, String param) {
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
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！"+e);
			e.printStackTrace();
		}
		//使用finally块来关闭输出流、输入流
		finally{
			try{
				if(out!=null){
					out.close();
				}
				if(in!=null){
					in.close();
				}
			}
			catch(IOException ex){
				ex.printStackTrace();
			}
		}
		return result;
	}
}
