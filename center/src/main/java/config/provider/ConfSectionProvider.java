package config.provider;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.google.inject.Singleton;

import config.BaseProvider;
import config.bean.ConfSection;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class ConfSectionProvider extends BaseProvider<ConfSection> {

	private ConfSection equipSection;

	private ConfSection partSection;

	private ConfSection timeSection;

	private ConfSection militarySection;// 军工副本

	public static ConfSectionProvider getInst() {
		return BeanManager.getBean(ConfSectionProvider.class);
	}

	public ConfSectionProvider() {
		super("", "");
	}

	@Override
	protected Class<ConfSection[]> getClassType() {
		return ConfSection[].class;
	}

	@Override
	protected ConfSection convertDBResult2Bean(ASObject o) {
		ConfSection conf = new ConfSection();
		conf.setSectionId(o.getInt("sectionId"));
		conf.setRank(o.getInt("rank"));
		conf.setBox1(JSON.parseObject(o.getString("box1"), List.class));
		conf.setBox2(JSON.parseObject(o.getString("box2"), List.class));
		conf.setBox3(JSON.parseObject(o.getString("box3"), List.class));
		conf.setType(o.getInt("type"));
		conf.setEndId(o.getInt("endId"));
		conf.setStartId(o.getInt("startId"));
		return conf;
	}

	@Override
	protected ASObject convertBean2DbData(ConfSection conf) {
		ASObject o = new ASObject();
		return o;
	}

	@Override
	public void init() {
		getAllConfig().forEach(e -> {
			if (e.getType() == 2) {
				equipSection = e;
			} else if (e.getType() == 3) {
				partSection = e;
			} else if (e.getType() == 4) {
				timeSection = e;
			} else if (e.getType() == 6) {
				militarySection = e;
			}
		});
	}

	public ConfSection getEquipSection() {
		return equipSection;
	}

	public ConfSection getPartSection() {
		return partSection;
	}

	public ConfSection getTimeSection() {
		return timeSection;
	}

	public ConfSection getMilitarySection() {
		return militarySection;
	}
}
