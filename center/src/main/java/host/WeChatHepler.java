package host;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class WeChatHepler {

	public static boolean isAllowed(String url) {
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost httppost = new HttpPost(url);
		httppost.setHeader("User-Agent", "Mozilla/5.0 (Linux; U; Android 2.3.6; zh-cn; GT-S5660 Build/GINGERBREAD) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1 MicroMessenger/4.5.255");
		httppost.setHeader("Referer", "https://mp.weixin.qq.com");
		try {
			CloseableHttpResponse response = client.execute(httppost);
			HttpEntity entity = response.getEntity();
			System.out.println("Login form get: " + response.getStatusLine());
			String result = EntityUtils.toString(entity, "utf-8");
			System.out.println(result);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	public static void main(String[] args) {
		String url = "http://www.sjzpuke.cn/";
		isAllowed(url);
	}
}
