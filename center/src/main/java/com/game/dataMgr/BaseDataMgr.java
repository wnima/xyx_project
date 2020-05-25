package com.game.dataMgr;

import java.util.List;

import javax.annotation.PostConstruct;

public abstract class BaseDataMgr {
	@PostConstruct
	abstract public void init();

	protected int calcProbWeights(List<List<Integer>> list, int pos) {
		int weights = 0;
		for (int i = 0; i < list.size(); i++) {
			weights += list.get(i).get(pos);
		}
		return weights;
	}
}
