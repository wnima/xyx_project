package config.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.game.util.RandomHelper;
import com.google.inject.Singleton;

import config.BaseProvider;
import config.bean.ConfMineForm;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class ConfMineFormProvider extends BaseProvider<ConfMineForm> {

	private Map<Integer, List<ConfMineForm>> formMap = new HashMap<>();

	public static ConfMineFormProvider getInst() {
		return BeanManager.getBean(ConfMineFormProvider.class);
	}

	public ConfMineFormProvider() {
		super("", "");
	}

	@Override
	protected Class<ConfMineForm[]> getClassType() {
		return ConfMineForm[].class;
	}

	@Override
	protected ConfMineForm convertDBResult2Bean(ASObject o) {
		ConfMineForm conf = new ConfMineForm();
		conf.setKeyId(o.getInt("keyId"));
		conf.setLv(o.getInt("lv"));
		conf.setForm(JSON.parseObject(o.getString("form"), List.class));
		conf.setAttr(JSON.parseObject(o.getString("attr"), List.class));
		return conf;
	}

	@Override
	protected ASObject convertBean2DbData(ConfMineForm conf) {
		ASObject o = new ASObject();
		return o;
	}

	@Override
	public void init() {
		for (ConfMineForm staticMineForm : getAllConfig()) {
			List<ConfMineForm> one = formMap.get(staticMineForm.getLv());
			if (one == null) {
				one = new ArrayList<>();
				formMap.put(staticMineForm.getLv(), one);
			}
			one.add(staticMineForm);
		}
	}

	public ConfMineForm randomForm(int lv) {
		List<ConfMineForm> one = formMap.get(lv);
		return one.get(RandomHelper.randomInSize(one.size()));
	}

	public boolean checkForm(ConfMineForm confMineForm) {
		int formCount = 0;
		for (List<Integer> e : confMineForm.getForm()) {
			if (!e.isEmpty()) {
				formCount++;
			}
		}

		int attrCount = 0;
		for (List<Integer> e : confMineForm.getAttr()) {
			if (!e.isEmpty()) {
				attrCount++;
			}
		}

		if (formCount != attrCount) {
			// System.err.println("check StaticMineForm " +
			// staticMineForm.getKeyId() + " |" + formCount + " " + attrCount);
			return false;
		}

		return true;
	}
}
