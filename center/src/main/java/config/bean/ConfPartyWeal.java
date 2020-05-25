package config.bean;

import java.util.List;

import config.IConfigBean;

/**
 * @author ChenKui
 * @version 创建时间：2015-9-10 下午2:02:26
 * @declare
 */

public class ConfPartyWeal implements IConfigBean {

	private int wealLv;
	private List<List<Integer>> wealList;

	public int getWealLv() {
		return wealLv;
	}

	public void setWealLv(int wealLv) {
		this.wealLv = wealLv;
	}

	public List<List<Integer>> getWealList() {
		return wealList;
	}

	public void setWealList(List<List<Integer>> wealList) {
		this.wealList = wealList;
	}

	@Override
	public int getId() {
		return 0;
	}

}
