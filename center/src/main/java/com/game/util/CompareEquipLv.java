package com.game.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import data.bean.Equip;

/**
 * @author ChenKui
 * @version 创建时间：2015-9-12 上午11:28:18
 * @declare
 */

public class CompareEquipLv implements Comparator<Equip> {

	@Override
	public int compare(Equip o1, Equip o2) {
		if (o1.getLv() < o2.getLv()) {
			return -1;
		} else if (o1.getLv() > o2.getLv()) {
			return 1;
		}
		return 0;
	}

	// public static void main(String[] args) {
	// List<Equip> equipList = new ArrayList<Equip>();
	// for (int i = 0; i < 30; i++) {
	// Equip eq = new Equip(i + 1, 701, RandomHelper.randomInSize(100), 0, 0);
	// equipList.add(eq);
	// }
	//
	// Collections.sort(equipList, new CompareEquipLv());
	//
	// for (Equip e : equipList) {
	// System.out.println(e.getEquipId() + "|" + e.getLv());
	// }
	// }
}
