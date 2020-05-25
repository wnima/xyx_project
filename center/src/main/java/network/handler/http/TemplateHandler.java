package network.handler.http;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import annotation.Path;
import config.provider.ConfSettingProvider;
import http.HttpResultUtil;
import util.ASObject;

@Path("/template")
public class TemplateHandler {

	private static final Logger logger = LoggerFactory.getLogger(TemplateHandler.class);

	@Path("/realodGameSetting")
	public Map<String, Object> realodGameSetting(ASObject params) {
		logger.info("reload game setting {}", params);
		ConfSettingProvider.getInst().reLoad();
		return HttpResultUtil.resultOk();
	}
	
}
