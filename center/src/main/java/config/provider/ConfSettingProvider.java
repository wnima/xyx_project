package config.provider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.HashBasedTable;
import com.google.inject.Singleton;

import config.BaseProvider;
import config.bean.ConfSetting;
import database.DataQueryResult;
import inject.BeanManager;
import json.JsonUtil;
import util.ASObject;

@Singleton
public class ConfSettingProvider extends BaseProvider<ConfSetting> {
	private static final Logger logger = LoggerFactory.getLogger(ConfSettingProvider.class);

	private HashBasedTable<String, Integer, ConfSetting> settings = HashBasedTable.create();

	public ConfSettingProvider() {
		super(null, "conf_setting");
	}

	public static ConfSettingProvider getInst() {
		return BeanManager.getBean(ConfSettingProvider.class);
	}

	@Override
	protected Class<ConfSetting[]> getClassType() {
		return ConfSetting[].class;
	}

	public void reloadOneRecord(ASObject o) {
		String key = o.getString("key");
		int pgNo = o.getInt("platNo");
		ConfSetting gameSetting = settings.get(key, pgNo);
		if (gameSetting != null) {
			gameSetting.setValue(JsonUtil.getGson().fromJson(o.get("value").toString(), ASObject.class));
		}
	}

	public ConfSetting get(String key, int pgNo) {
		return settings.get(key, pgNo);
	}

	public ConfSetting get(String key) {
		return settings.get(key, 0);
	}

	public ConfSetting queryByKey(String key) {
		Map<String, Object> where = new HashMap<String, Object>();
		where.put("key", key);
		List<ASObject> list = DataQueryResult.load("conf_setting", where);
		if (list.isEmpty()) {
			return get(key);
		}
		ASObject o = list.stream().findFirst().get();
		reloadOneRecord(o);
		return get(key);
	}

	@Override
	public void reLoad() {
		super.reLoad();
		settings.clear();
		this.init();
	}

	@Override
	protected ConfSetting convertDBResult2Bean(ASObject o) {
		ConfSetting gameSetting = new ConfSetting();
		gameSetting.setId(o.getInt("id"));
		gameSetting.setKey(o.getString("key"));
		gameSetting.setPgNo(o.getInt("platNo"));
		gameSetting.setValue(JsonUtil.getGson().fromJson(o.getString("value"), ASObject.class));
		return gameSetting;
	}

	@Override
	protected ASObject convertBean2DbData(ConfSetting gloGameSetting) {
		ASObject o = new ASObject();
		o.put("id", gloGameSetting.getId());
		o.put("key", gloGameSetting.getKey());
		o.put("platNo", gloGameSetting.getPgNo());
		o.put("value", gloGameSetting.getValue().toString());
		return o;
	}

	@Override
	protected void init() {
		getAllConfig().forEach(e -> {
			settings.put(e.getKey(), e.getPgNo(), e);
		});
	}

}
