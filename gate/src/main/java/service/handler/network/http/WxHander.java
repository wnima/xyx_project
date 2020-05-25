package service.handler.network.http;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import annotation.Path;
import http.HttpResultUtil;

@Path("/weix")
public class WxHander {

	private static final Logger logger = LoggerFactory.getLogger(WxHander.class);

	@Path("/payCallback")
	public Map<String, Object> vipCharge(Map<String, String> params) {

		logger.info("payCallback:{}", params);

		String openid = params.get("openid");
		String itopid = params.get("itopid");
		String appid = params.get("appid");
		String ts = params.get("ts");
		String payitem = params.get("payitem");
		String token = params.get("token");
		String billno = params.get("billno");
		String version = params.get("version");
		String zoneid = params.get("zoneid");
		String providetype = params.get("providetype");
		String amt = params.get("amt");
		String appmeta = params.get("appmeta");
		String cftid = params.get("cftid");
		String channel_id = params.get("channel_id");
		String clientver = params.get("clientver");
		String payamt_coins = params.get("payamt_coins");
		String pubacct_payamt_coins = params.get("pubacct_payamt_coins");
		String bazinga = params.get("bazinga");
		String sig = params.get("sig");

		return HttpResultUtil.resultOk();
	}

	@Path("/test")
	public Map<String, Object> test(Map<String, String> params) {
		logger.info("params:{}", params);
		return HttpResultUtil.resultOk();
	}
}
