package com.game.dataMgr;

import java.util.ArrayList;
import java.util.List;

import com.game.util.RandomHelper;

import config.bean.ConfIniLord;
import config.bean.ConfIniName;


// done
public class StaticIniDataMgr extends BaseDataMgr {

	private List<String> markList = new ArrayList<String>();
	private List<String> familyList = new ArrayList<String>();
	private List<String> manList = new ArrayList<String>();
	private List<String> womanList = new ArrayList<String>();

	@Override
	public void init() {
		this.initName();
	}

	private void initName() {
//		List<StaticIniName> staticNameList = staticDataDao.selectName();
//		for (StaticIniName staticName : staticNameList) {
//			String familyName = staticName.getFamilyname();
//			String womanName = staticName.getWomanname();
//			String manName = staticName.getManname();
//			String mark = staticName.getMark();
//			if (familyName != null && !familyName.equals("")) {
//				familyList.add(familyName);
//			}
//
//			if (womanName != null && !womanName.equals("")) {
//				womanList.add(womanName);
//			}
//
//			if (manName != null && !manName.equals("")) {
//				manList.add(manName);
//			}
//
//			if (mark != null && !mark.equals("")) {
//				markList.add(mark);
//			}
//		}
	}

	public String getManNick() {
		StringBuffer sb = new StringBuffer();

		int familyIndex = RandomHelper.randomInSize(familyList.size());
		sb.append(familyList.get(familyIndex));

		int nameIndex = RandomHelper.randomInSize(manList.size());
		sb.append(manList.get(nameIndex));
		return sb.toString();
	}

	public String getWomanNick() {
		StringBuffer sb = new StringBuffer();

		int familyIndex = RandomHelper.randomInSize(familyList.size());
		sb.append(familyList.get(familyIndex));

		int nameIndex = RandomHelper.randomInSize(womanList.size());
		sb.append(womanList.get(nameIndex));
		return sb.toString();
	}

//	public StaticIniLord getLordIniData() {
//		return staticDataDao.selectLord();
//	}
}
