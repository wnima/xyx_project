package http;

import java.util.HashMap;
import java.util.Map;

public class HttpResultUtil {
	public static Map<String, Object> resultOk() {
		Map<String, Object> result = new HashMap<>();
		result.put("result_code", 200);
		result.put("err_msg", "");
		return result;
	}

	public static Map<String, Object> returnError(int errorCode, String errorMsg) {
		Map<String, Object> result = new HashMap<>();
		result.put("result_code", errorCode);
		result.put("err_msg", errorMsg);
		return result;
	}

	public static Map<String, Object> returnTsdPayCallBack(int retCode, String retMsg) {
		Map<String, Object> result = new HashMap<>();
		result.put("retCode", retCode);
		result.put("retMsg", retMsg);
		return result;
	}
}
