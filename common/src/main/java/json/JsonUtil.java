package json;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import config.IConfigBean;
import config.IParse;
import util.ASObject;

public class JsonUtil {

	private static ThreadLocal<Gson> gsonList = new ThreadLocal<>();

	public static <T> Map<Integer, T> getJsonMap(Class<T[]> classType, String jsonPath) {
		Map<Integer, T> resultMap = new HashMap<>();
		T[] result = getGson().fromJson(getJsonString(jsonPath), classType);
		for (T t : result) {
			if (t instanceof IConfigBean) {
				IConfigBean bean = (IConfigBean) t;
				if (bean instanceof IParse) {
					((IParse) bean).parse();
				}
				resultMap.put(bean.getId(), (T) bean);
			}
		}
		return resultMap;
	}

	public static Gson getGson() {
		Gson gson = gsonList.get();
		if (gson == null) {
			gson = new Gson();
			gsonList.set(gson);
		}
		return gson;
	}

	public static String getJsonString(String jsonPath) {
		InputStream in = null;
		try {
			in = new FileInputStream(jsonPath);
			int length = in.available();
			byte[] bytes = new byte[length];
			in.read(bytes, 0, length);
			return new String(bytes, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static String getJsonString(Object obj) {
		return getGson().toJson(obj);
	}

	public <T> T fromJson(String jsonStr) {
		Type type = new TypeToken<T>() {
		}.getType();
		return getGson().fromJson(jsonStr, type);
	}

	public static void main(String[] args) {
		String array = "[[122,12,12,2],[1232,1232,1232,1]]";
//		ASObject as = new ASObject();
		Map<String, Object> map = new HashMap<>();
		map.put("u", "ok");
		ASObject as = new ASObject(map);
		String s = as.toString();
		System.out.println(s);
		List<List<Integer>> li = JSON.parseObject(array, List.class);
//		System.out.println(li);

		System.out.println(li.toString());
	}
}
