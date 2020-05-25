package service.handler.network.http;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import annotation.Path;
import http.HttpResultUtil;

@Path("/Text")
public class TextHander {

	private static final Logger logger = LoggerFactory.getLogger(TextHander.class);

	@Path("/test")
	public Map<String, Object> test(Map<String, String> params) {
		logger.info("params:{}", params);
		return HttpResultUtil.resultOk();
	}
}
