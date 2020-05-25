package com.game.util;

import java.util.Comparator;

import data.bean.PartyRank;

/**
 * @author ChenKui
 * @version 创建时间：2015-9-12 上午11:28:18
 * @declare
 */

public class CompareParty implements Comparator<PartyRank> {

	@Override
	public int compare(PartyRank o1, PartyRank o2) {
		if (o1.getFight() > o2.getFight()) {
			return -1;
		} else if (o1.getFight() < o2.getFight()) {
			return 1;
		}
		return 0;
	}

}

