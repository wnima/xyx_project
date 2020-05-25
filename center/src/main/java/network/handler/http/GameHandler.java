package network.handler.http;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import annotation.Path;
import http.HttpResultUtil;
import util.ASObject;

@Path("/game")
public class GameHandler {

	private static final Logger logger = LoggerFactory.getLogger(GameHandler.class);

	@Path("/reloadTable")
	public Map<String, Object> reloadTable(ASObject params) {
		if (params == null || !params.containsKey("tableName")) {
			return HttpResultUtil.returnError(201, "param error");
		}
		String tableName = params.getString("tableName");
		return HttpResultUtil.resultOk();
	}

	@Path("/channel")
	public Map<String, Object> gamePlat(ASObject params) {
		logger.info(" with gamePlat {}", params);
		if (params == null || !params.containsKey("channels")) {
			return HttpResultUtil.returnError(201, "param error");
		}
		try {
			Object result = params.get("channels");
			List<String> list = (List<String>) result;
			for (String e : list) {
				logger.info("channel e:{}", Integer.valueOf(e));
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return HttpResultUtil.resultOk();
	}

}
