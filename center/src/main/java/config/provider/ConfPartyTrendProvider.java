package config.provider;

import com.google.inject.Singleton;

import config.BaseProvider;
import config.bean.ConfPartyTrend;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class ConfPartyTrendProvider extends BaseProvider<ConfPartyTrend> {

	public static ConfPartyTrendProvider getInst() {
		return BeanManager.getBean(ConfPartyTrendProvider.class);
	}

	public ConfPartyTrendProvider() {
		super("", "");
	}

	@Override
	protected Class<ConfPartyTrend[]> getClassType() {
		return ConfPartyTrend[].class;
	}

	@Override
	protected ConfPartyTrend convertDBResult2Bean(ASObject o) {
		ConfPartyTrend conf = new ConfPartyTrend();
		conf.setTrendId(o.getInt("trendId"));
		conf.setType(o.getInt("type"));
		conf.setName(o.getString("name"));
		conf.setContent(o.getString("content"));
		conf.setParam(o.getString("param"));
		return conf;
	}

	@Override
	protected ASObject convertBean2DbData(ConfPartyTrend conf) {
		ASObject o = new ASObject();
		return o;
	}

	@Override
	public void init() {
	}

}
