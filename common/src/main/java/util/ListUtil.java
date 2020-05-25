package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListUtil {

	public static List<Integer> shuffleList(int[] array) {
		List<Integer> list = new ArrayList<Integer>();
		for (Integer e : array) {
			list.add(e);
		}
		Collections.shuffle(list);
		return list;
	}

	public static List<Integer> toList(int[] array) {
		List<Integer> list = new ArrayList<Integer>();
		for (Integer e : array) {
			list.add(e);
		}
		return list;
	}

}
