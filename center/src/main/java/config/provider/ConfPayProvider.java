package config.provider;

import com.google.inject.Singleton;

import config.BaseProvider;
import config.bean.ConfPay;
import inject.BeanManager;
import util.ASObject;

@Singleton
public class ConfPayProvider extends BaseProvider<ConfPay> {

	public static ConfPayProvider getInst() {
		return BeanManager.getBean(ConfPayProvider.class);
	}

	public ConfPayProvider() {
		super("", "");
	}

	@Override
	protected Class<ConfPay[]> getClassType() {
		return ConfPay[].class;
	}

	@Override
	protected ConfPay convertDBResult2Bean(ASObject o) {
		ConfPay conf = new ConfPay();
		conf.setPayId(o.getInt("payId"));
		conf.setTopup(o.getInt("topup"));
		conf.setExtraGold(o.getInt("extraGold"));
		return conf;
	}

	@Override
	protected ASObject convertBean2DbData(ConfPay conf) {
		ASObject o = new ASObject();
		return o;
	}

	@Override
	public void init() {
	}

}
