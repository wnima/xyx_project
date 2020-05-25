package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import service.GateApp;

public class TSDKUtil {
	private static Logger logger = LoggerFactory.getLogger(TSDKUtil.class);

	public static boolean checkTSDkSign(Map<String, String> params, String check) {
		String s = getTSDKParamStr(params);
		s = s + "#" + GateApp.tSDKAppKey;
		String sign = MD5Util.MD5(s);
		logger.info("check:{} sign:{} signStr:{}", check, sign.toLowerCase(), s);
		if (sign.endsWith(check.toUpperCase())) {
			return true;
		}
		return false;
	}

	public static String getTSDKParamStr(Map<String, String> param) {
		List<Map.Entry<String, String>> list = new ArrayList<Map.Entry<String, String>>(param.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, String>>() {
			public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
				return o1.getKey().compareTo(o2.getKey());
			}
		});

		StringBuilder str = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			Map.Entry<String, String> m = list.get(i);
			if (m.getKey().equals("HJSign")) {// 签名参数不参与签名
				continue;
			}
			if (m.getValue().equals("")) {// 空值不参与计算
				continue;
			}
			str.append(m.getValue());
			if (i < param.size() - 1) {
				str.append("#");
			}
		}
		return str.toString();
	}

	public static void main(String[] args) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("HJCurrency", "CNY");
		map.put("HJSign", "772f88eb3f7a3003a4ddced4049e4563");
		map.put("HJRoleId", "2203081");
		map.put("HJPayExt", "111");
		map.put("HJOrderTime", "2019-10-14 16:55:10");
		map.put("HJUniqueId", "1910143744446770");
		map.put("HJAppId", "590");
		map.put("HJItemId", "");
		map.put("HJItemName", "月卡");
		map.put("HJUserId", "ccmg|d252669099");
		map.put("HJAmount", "1");
		map.put("HJChannel", "ccmg");
		map.put("HJOrderId", "201910141517084771119");
		map.put("HJServerId", "1");

		String url = "http://47.75.125.209:8080/tsdk/payCallback";
//		String url = "http://192.168.12.158:8080/tsdk/payCallback";
		String ret = HttpUtil.sendGet(url, map);
		System.out.println(ret);

	}
}
