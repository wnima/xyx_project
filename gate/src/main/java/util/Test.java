package util;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import util.open.SnsNetwork;
import util.open.SnsSigCheck;

public class Test {

	static final String APP_ID = "1109843864";// APPID
	static final String secretKey = "fzPD3z8syhWasB9B20bbvJWhHcXQeaXT" + "&";// 现网
	// static final String secretKey = "WSsVuVNkU5fzcsyJ" + "&";//沙箱

	// 所需参数
	static final String openid = "onI2Zt4rN_AeILympVfAq8L8opns";
	static final String openkey = "26_gkio9j6Uv5WDFJevnEPTnVOqSKVBrbe2pAp7ilix2iuzAy9IRGcPUEQKvuTO3JTVdKvMslwnSsTeYlBx6wYhz4ApWVQUyL4U96NwO_7TaOc";
	static final String pf = "desktop_m_wx-00000000-android-00000000-864619046302016-ysdkwater";
	static final String pfkey = "ed34721fcda50c9f0f99aa025bef783e";
	static final String ts = "1569918117";
	static final String zoneid = "1";

	public static void main(String[] args) {
		try {

			String method = "GET";
			String urlPath = "/v3/r/mpay/get_balance_m";
			String url = "https://ysdk.qq.com/v3/r/mpay/get_balance_m";

			HashMap<String, String> params = new HashMap<String, String>();
			params.put("openid", openid);
			params.put("openkey", openkey);
			params.put("appid", APP_ID);// 应用ID
			params.put("pf", pf);
			params.put("pfkey", pfkey);
			params.put("ts", ts);
			params.put("zoneid", zoneid);

			// secretKey的值未appkey值+&
			String sig = SnsSigCheck.makeSig(method, urlPath, params, secretKey);
			params.put("sig", SnsSigCheck.encodeValue(sig));
			System.out.println("sign：" + sig);

			HashMap<String, String> cookies = new HashMap<String, String>();
			cookies.put("session_id", "hy_gameid");
			cookies.put("session_type", "wc_actoken");
			cookies.put("org_loc", SnsSigCheck.encodeUrl(urlPath));
			System.out.println("cookies:" + cookies);

			String ret = SnsNetwork.postRequest(url, params, cookies, "http");
			System.out.println("ret:" + ret);
//			HttpUtil.doGet(urlPath, cookie)

		} catch (Exception e) {
		}
	}

}
