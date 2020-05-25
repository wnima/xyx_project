package service.handler.module.client;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.google.inject.Singleton;
import com.google.protobuf.MessageLite;

import actor.GateActorManager;
import inject.BeanManager;
import io.netty.channel.ChannelHandlerContext;
import network.AbstractHandlers;
import network.AbstractHandlers.MessageHolder;
import network.IModuleMessageHandler;
import packet.CocoPacket;
import pb.LoginPb.LoginReq;
import pb.LoginPb.QQLoginReq;
import pb.ServerInternal.PBInterLoginRq;
import pb.ServerInternal.PBInterQQLoginRq;
import protocol.c2s.RequestCode;
import service.GateApp;
import service.handler.agent.CocoAgent;
import util.HttpUtil;
import util.NettyUtil;

@Singleton
public class AccountModule implements IModuleMessageHandler {

	private static Logger logger = LoggerFactory.getLogger(AccountModule.class);

	public static AccountModule getInst() {
		return BeanManager.getBean(AccountModule.class);
	}

	@Override
	public void registerModuleHandler(AbstractHandlers handler) {
		handler.registerAction(RequestCode.LOGIN.getValue(), this::actionLoginRq, LoginReq.getDefaultInstance());
		handler.registerAction(RequestCode.QQ_LOGIN.getValue(), this::actionQQLoginRq, QQLoginReq.getDefaultInstance());
	}

	private void actionLoginRq(ChannelHandlerContext client, CocoPacket packet, MessageHolder<MessageLite> message) {
		LoginReq req = message.get();
		logger.info("actionLoginRq req:{}", req);
		CocoAgent agent = NettyUtil.getAttribute(client, "agent");
		
		PBInterLoginRq.Builder builder = PBInterLoginRq.newBuilder();
		builder.setDeviceNo(req.getDeviceNo());
		builder.setName(req.getName());
		builder.setPw(req.getPw());
		builder.setSessionId(agent.getSessionId());
		builder.setGameType(req.getGameType());

		GateApp.getInst().getClient().sendRequest(new CocoPacket(RequestCode.INTERNAL_LOGIN_RQ, builder.build()));
	}

	private void actionQQLoginRq(ChannelHandlerContext client, CocoPacket packet, MessageHolder<MessageLite> message) {
		QQLoginReq req = message.get();
		logger.info("actionQQLoginRq req:{}", req);
		CocoAgent agent = NettyUtil.getAttribute(client, "agent");
		
		String appId = GateApp.qq_id;
		String secret = GateApp.qq_key;
		String url = "https://api.q.qq.com/sns/jscode2session?appid=" + appId + "&secret=" + secret + "&js_code=" + req.getAccessToken() + "&grant_type=authorization_code";
		GateActorManager.getHttpActor().put(() -> {
			String result = HttpUtil.doGet(url, null);
			logger.info("actionQQLoginRq result:{}", result);
			if (result == null) {
				return null;
			}

			JSONObject r = JSONObject.parseObject(result);
			int errcode = r.getIntValue("errcode");
			if (errcode == 0) {
				PBInterQQLoginRq.Builder builder = PBInterQQLoginRq.newBuilder();
				builder.setSessionId(agent.getSessionId());
				builder.setPlatNo(1);
				builder.setPlatId(r.getString("openid"));
				builder.setSessionKey(r.getString("session_key"));
				builder.setUnionid(r.getString("unionid"));
				builder.setGameType(req.getGameType());
				GateApp.getInst().getClient().sendRequest(new CocoPacket(RequestCode.INTERNAL_QQ_LOGIN_RQ, builder.build()));
				logger.info("qq login success platNo:{} platId:{} sessionKey:{}", 1, r.getString("openid"), r.getString("session_key"));
			} else {
				String errmsg = r.getString("errmsg");
			}
			return null;
		});
	}

	public static void main(String[] args) {
		String appId = "1110488182";
		String secret = "TeZFLnU2q9u53ZZ6";
		String token = "0b91dd5d019aff61f848989edba1b14d";

		String access_token = "8SVtUbRlgnXWGSL6VoWB6j2blIqdJBd7hPVuDPcwFAxTxjhIOaRHnEnIU9Axigh-9pw";

		String code = "8c6332c316220dc04301d5a9d8f52f5e";
		String url = "https://api.q.qq.com/sns/jscode2session?appid=" + appId + "&secret=" + secret + "&js_code=" + code + "&grant_type=authorization_code";
//		String url = "https://api.q.qq.com/sns/jscode2session?appid=1110488182&secret=TeZFLnU2q9u53ZZ6&js_code=8c6332c316220dc04301d5a9d8f52f5e&grant_type=authorization_code";
		String result = HttpUtil.doGet(url, null);
		System.out.println(result);

//		String host = "https://api.q.qq.com/sns/jscode2session";
//		Map<String, String> parameter = new HashMap<String, String>();
//		parameter.put("appId", appId);
//		parameter.put("secret", secret);
//		parameter.put("js_code", code);
//		parameter.put("grant_type", "authorization_code");
//		String result1 = HttpUtil.sendGet(host, parameter);
//		System.out.println(result1);
//		if (result1 == null) {
//			return;
//		}
//
//		JSONObject r = JSONObject.parseObject(result);
//		int errcode = r.getIntValue("errcode");
//		if (errcode == 0) {
//			String openId = r.getString("openid");
//			String session_key = r.getString("session_key");
//			String unionid = r.getString("unionid");
//
//			
//			if (access_token != null) {
//				getUserInfo(appId, access_token, openId);
//			}
//
//		}
	}

	public static String getAccessToken(String appId, String secret) {
		String url = "https://api.q.qq.com/api/getToken?grant_type=client_credential&appid=" + appId + "&secret=" + secret;
		String result = HttpUtil.doGet(url, null);
//		logger.info("getAccessToken result:{}", result);
		System.out.println("getAccessToken:" + result);
		if (result == null) {
			return null;
		}
		JSONObject r = JSONObject.parseObject(result);
		int errcode = r.getIntValue("errcode");
		if (errcode == 0) {
			return r.getString("access_token");
		}
		return null;
	}

	public static Map<String, Object> getUserInfo(String appId, String access_token, String openId) {
		String url = "https://graph.qq.com/user/get_simple_userinfo?access_token=" + access_token + "&oauth_consumer_key=" + appId + "&openid=" + openId + "&format=json";
		String result = HttpUtil.doGet(url, null);
		System.out.println("userInfo:" + result);
//		logger.info("getUserInfo result:{}", result);
		if (result == null) {
			return null;
		}
		JSONObject r = JSONObject.parseObject(result);
		return null;

	}

}
