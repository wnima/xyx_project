package config.provider;

import java.util.ArrayList;
import java.util.List;

import com.game.util.RandomHelper;
import com.google.inject.Singleton;

import config.BaseProvider;
import config.bean.ConfIniName;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class ConfIniNameProvider extends BaseProvider<ConfIniName> {

	private List<String> markList = new ArrayList<String>();
	private List<String> familyList = new ArrayList<String>();
	private List<String> manList = new ArrayList<String>();
	private List<String> womanList = new ArrayList<String>();

	public static ConfIniNameProvider getInst() {
		return BeanManager.getBean(ConfIniNameProvider.class);
	}

	public ConfIniNameProvider() {
		super("", "");
	}

	@Override
	protected Class<ConfIniName[]> getClassType() {
		return ConfIniName[].class;
	}

	@Override
	protected ConfIniName convertDBResult2Bean(ASObject o) {
		ConfIniName conf = new ConfIniName();
		conf.setFamilyname(o.getString("familyname"));
		conf.setManname(o.getString("manname"));
		conf.setWomanname(o.getString("womanname"));
		conf.setMark(o.getString("mark"));
		return conf;
	}

	@Override
	protected ASObject convertBean2DbData(ConfIniName conf) {
		ASObject o = new ASObject();
		return o;
	}

	@Override
	public void init() {
		getAllConfig().forEach(e -> {
			String familyName = e.getFamilyname();
			String womanName = e.getWomanname();
			String manName = e.getManname();
			String mark = e.getMark();
			if (familyName != null && !familyName.equals("")) {
				familyList.add(familyName);
			}

			if (womanName != null && !womanName.equals("")) {
				womanList.add(womanName);
			}

			if (manName != null && !manName.equals("")) {
				manList.add(manName);
			}

			if (mark != null && !mark.equals("")) {
				markList.add(mark);
			}
		});
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
}
