package config.provider;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.google.inject.Singleton;

import config.BaseProvider;
import config.bean.ConfSign;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class ConfSignProvider extends BaseProvider<ConfSign> {

	private static Logger logger = LoggerFactory.getLogger(ConfSignProvider.class);

	public static ConfSignProvider getInst() {
		return BeanManager.getBean(ConfSignProvider.class);
	}

	public ConfSignProvider() {
		super("", "conf_sign");
	}

	@Override
	protected Class<ConfSign[]> getClassType() {
		return ConfSign[].class;
	}

	@Override
	protected ConfSign convertDBResult2Bean(ASObject o) {
		ConfSign conf = new ConfSign();
		conf.setSignId(o.getInt("signId"));
		conf.setSignDay(o.getInt("signDay"));
		conf.setAwardList(JSON.parseObject(o.getString("awardList"), List.class));
		return conf;
	}

	@Override
	protected String getIdKey() {
		return "signId";
	}

	@Override
	protected ASObject convertBean2DbData(ConfSign conf) {
		ASObject o = new ASObject();
		o.put("signId", conf.getSignId());
		o.put("signDay", conf.getSignDay());
		return o;
	}

	@Override
	public void init() {
		getAllConfig().forEach(e -> {
			logger.info("signId:{} signDay:{} awardList:{}", e.getSignId(), e.getSignDay(), e.getAwardList());
		});
	}

}
