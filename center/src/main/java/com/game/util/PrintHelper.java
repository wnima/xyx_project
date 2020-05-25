package com.game.util;

import java.util.List;

public class PrintHelper {
	public static final boolean ENABALE = true;
	public static final boolean FIGHT_ENABALE = true;

	static public void println(Object x) {
		if (ENABALE) {
			System.out.println(x);
		}
	}

	static public void printFight(Object x) {
		if (FIGHT_ENABALE) {
			System.out.println(x);
		}
	}

	static public void printIntList(String prom, List<Integer> list) {
		System.out.print(prom);
		for (int i = 0; i < list.size(); i++) {
			System.out.print(list.get(i) + "|");
		}
		System.out.print("\r\n");
	}
}
