package config.provider;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.google.inject.Singleton;

import config.BaseProvider;
import config.bean.ConfActCourse;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class ConfActCourseProvider extends BaseProvider<ConfActCourse> {

	public static ConfActCourseProvider getInst() {
		return BeanManager.getBean(ConfActCourseProvider.class);
	}

	public ConfActCourseProvider() {
		super("", "");
	}

	@Override
	protected Class<ConfActCourse[]> getClassType() {
		return ConfActCourse[].class;
	}

	@Override
	protected ConfActCourse convertDBResult2Bean(ASObject o) {
		ConfActCourse conf = new ConfActCourse();
		conf.setCourseId(o.getInt("courseId"));
		conf.setActivityId(o.getInt("activityId"));
		conf.setSctionId(o.getInt("sctionId"));
		conf.setDeno(o.getInt("deno"));
		conf.setDropList(JSON.parseObject(o.getString("dropList"), List.class));
		return conf;
	}

	@Override
	protected ASObject convertBean2DbData(ConfActCourse conf) {
		ASObject o = new ASObject();
		o.put("courseId", conf.getCourseId());
		o.put("activityId", conf.getActivityId());
		o.put("sctionId", conf.getSctionId());
		o.put("deno", conf.getDeno());
		o.put("dropList", conf.getDropList().toString());
		return o;
	}

	@Override
	public void init() {
	}

}
