package util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;

public class MapUtil {

	public static <T, E> Map<T, E> convert(String content, Class<T> t, Class<E> e) {
		Map<T, E> map = new HashMap<T, E>();
		if (StringUtils.isEmpty(content)) {
			return map;
		}

		List<Object> list = JSON.parseObject(content, List.class);
		for (int i = 0; i < list.size(); i++) {
			Object obj = list.get(i);
			if (obj instanceof List) {
				List<Object> entity = (List<Object>) obj;
				if (entity.size() < 2) {
					continue;
				}
				Object a = entity.get(0);
				Object b = entity.get(1);
				map.put((T) a, (E) b);
				continue;
			}
			i++;
			Object obj2 = list.get(i);
			map.put((T) obj, (E) obj2);
		}
		return map;
	}

	public static void main(String[] args) {
		String content = "[[1,2],[3,4],[5,6],[7,8]]";
		Map<Integer,Integer> re = convert(content, Integer.class, Integer.class);
		System.out.println(re);
	}
}
