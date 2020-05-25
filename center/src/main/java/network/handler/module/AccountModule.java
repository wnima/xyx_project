package network.handler.module;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;
import com.google.protobuf.MessageLite;

import actor.CenterActorManager;
import data.bean.User;
import data.provider.UserProvider;
import database.DataQueryResult;
import inject.BeanManager;
import io.netty.channel.ChannelHandlerContext;
import mxw.PlayerManager;
import mxw.UserData;
import network.AbstractHandlers;
import network.IModuleMessageHandler;
import packet.CocoPacket;
import pb.ServerInternal.PBInterLoginRq;
import pb.ServerInternal.PBInterLoginRsp;
import pb.ServerInternal.PBInterQQLoginRq;
import pb.ServerInternal.PBInterQQLoginRsp;
import pb.util.PBPairStringLong;
import protocol.c2s.RequestCode;
import util.ASObject;
import util.DateUtil;
import util.MsgHelper;

@Singleton
public class AccountModule implements IModuleMessageHandler {

	private static Logger logger = LoggerFactory.getLogger(AccountModule.class);

	public static AccountModule getInst() {
		return BeanManager.getBean(AccountModule.class);
	}

	@Override
	public void registerModuleHandler(AbstractHandlers handler) {
		handler.registerAction(RequestCode.CENTER_PLAYER_LOGOUT.getValue(), this::actionLogoutRq, PBPairStringLong.getDefaultInstance());
		handler.registerAction(RequestCode.INTERNAL_LOGIN_RQ.getValue(), this::actionLoginRq, PBInterLoginRq.getDefaultInstance());
		handler.registerAction(RequestCode.INTERNAL_QQ_LOGIN_RQ.getValue(), this::actionQQLoginRq, PBInterQQLoginRq.getDefaultInstance());
	}

	private void actionLogoutRq(ChannelHandlerContext ioSession, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> message) {
		PBPairStringLong req = message.get();
		UserData player = PlayerManager.getInst().getPlayerById(req.getValue());
		if (player != null) {
			player.setLogout(true);
			player.setLogoutCrons(0);
			player.setOnline(false);
		}
	}

	private void actionLoginRq(ChannelHandlerContext ioSession, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> message) {
		PBInterLoginRq req = message.get();

		String name = req.getName();
		String pwd = req.getPw();
		int gameType = req.getGameType();
		
		logger.info("actionLoginRq name:{} pwd:{}", name, pwd);
		// 登陆返回
		PBInterLoginRsp.Builder builder = PBInterLoginRsp.newBuilder();

		UserData player = PlayerManager.getInst().getPlayerByAccountAndGameType(name,gameType);
		if (player != null) {
			if (!player.getUser().getPwd().equals(pwd)) {// 密码错误
				builder.setMsg("密码错误");
				MsgHelper.sendRequest(ioSession, RequestCode.INTERNAL_LOGIN_RSP, builder.build());
				return;
			}

			builder.setMsg("登陆成功");
			builder.setUserId(player.getPlayerId());
			builder.setBan(player.getUser().getBan() == 0);
			builder.setBanMsg(player.getUser().getBanMsg() == null ? "" : player.getUser().getBanMsg());
			builder.setWhite(player.getUser().getWhite() == 1);
			builder.setSessionId(req.getSessionId());
			player.setOnline(true);
			player.setLogout(false);
			player.setLogoutCrons(0);
			MsgHelper.sendRequest(ioSession, RequestCode.INTERNAL_LOGIN_RSP, builder.build());
			return;
		} else {
			CenterActorManager.getLoadActor(0).put(() -> {
				UserData load = null;
				Map<String, Object> where = new HashMap<String, Object>();
				where.put("account", name);
				where.put("gameType", name);
				List<ASObject> list = DataQueryResult.load("p_user", where);
				String loginMsg = null;
				if (list.isEmpty()) {// 直接创建账号
					User user = new User();
					user.setAccount(name);
					user.setPwd(pwd);
					user.setProtrait(1);
					user.setUserName(name);
					user.setGold(30000);
					user.setCoin(30000);
					user.setPower(20);
					user.setCreateTime(DateUtil.getSecondTime());
					user.setLoginTime(DateUtil.getSecondTime());
					user.setGameType(gameType);
					UserProvider.getInst().insert(user);
					logger.info("注冊成功 userId:{}", user.getUserId());
					UserProvider.getInst().putBean(user);
					PlayerManager.getInst().loadUser(user);
					load = PlayerManager.getInst().init(user.getUserId());
					loginMsg = "登陆成功";
				} else {
					if (pwd.equals(list.get(0).getString("pwd"))) {
						loginMsg = "登陆成功";
						load = PlayerManager.getInst().getPlayerById(list.get(0).getLong("userId"));
					} else {
						loginMsg = "密码错误";
					}
				}

				builder.setMsg(loginMsg);
				if (load != null) {
					builder.setUserId(load.getPlayerId());
					builder.setBan(load.getUser().getBan() == 0);
					builder.setBanMsg(load.getUser().getBanMsg() == null ? "" : load.getUser().getBanMsg());
					builder.setWhite(load.getUser().getWhite() == 1);
					builder.setSessionId(req.getSessionId());
				}

				load.setOnline(true);
				load.setLogout(false);
				MsgHelper.sendRequest(ioSession, RequestCode.INTERNAL_LOGIN_RSP, builder.build());
				return null;
			});

		}

	}

	private void actionQQLoginRq(ChannelHandlerContext ioSession, CocoPacket packet, AbstractHandlers.MessageHolder<MessageLite> message) {
		PBInterQQLoginRq req = message.get();
		int platNo = req.getPlatNo();
		String platId = req.getPlatId();
		int gameType = req.getGameType();
		logger.info("actionQQLoginRq platNo:{} platId:{}", platNo, platId);

		// 登陆返回
		PBInterQQLoginRsp.Builder builder = PBInterQQLoginRsp.newBuilder();

		UserData player = PlayerManager.getInst().getPlayerByPlatId(platId);
		if (player != null) {// 用户已经存在
			builder.setMsg("登陆成功");
			builder.setUserId(player.getPlayerId());
			builder.setBan(player.getUser().getBan() == 0);
			builder.setBanMsg(player.getUser().getBanMsg() == null ? "" : player.getUser().getBanMsg());
			builder.setWhite(player.getUser().getWhite() == 1);
			builder.setSessionId(req.getSessionId());
			player.setOnline(true);
			player.setLogout(false);
			player.setLogoutCrons(0);
			MsgHelper.sendRequest(ioSession, RequestCode.INTERNAL_QQ_LOGIN_RSP, builder.build());
			return;
		} else {
			String sessionKey = req.getSessionKey();
//			String unionId = req.getUnionid();
			// 请求用户的相关信息

			CenterActorManager.getLoadActor(0).put(() -> {
				UserData load = null;
				Map<String, Object> where = new HashMap<String, Object>();
				where.put("platNo", req.getPlatNo());
				where.put("platId", req.getPlatId());
				where.put("gameType", gameType);
				List<ASObject> list = DataQueryResult.load("p_user", where);
				String loginMsg = null;
				if (list.isEmpty()) {// 直接创建账号
					User user = new User();
					user.setPlatNo(platNo);
					user.setPlatId(platId);
					user.setProtrait(1);
					user.setGold(30000);
					user.setCoin(30000);
					user.setPower(20);
					user.setCreateTime(DateUtil.getSecondTime());
					user.setLoginTime(DateUtil.getSecondTime());
					user.setGameType(gameType);
					UserProvider.getInst().insert(user);
					logger.info("注冊成功 userId:{}", user.getUserId());
					UserProvider.getInst().putBean(user);
					PlayerManager.getInst().loadUser(user);
					load = PlayerManager.getInst().init(user.getUserId());
					loginMsg = "登陆成功";
				} else {
					load = PlayerManager.getInst().getPlayerById(list.get(0).getLong("userId"));
				}

				builder.setMsg(loginMsg);
				if (load != null) {
					builder.setUserId(load.getPlayerId());
					builder.setBan(load.getUser().getBan() == 0);
					builder.setBanMsg(load.getUser().getBanMsg() == null ? "" : load.getUser().getBanMsg());
					builder.setWhite(load.getUser().getWhite() == 1);
					builder.setSessionId(req.getSessionId());
					load.setOnline(true);
					load.setLogout(false);
				}

				MsgHelper.sendRequest(ioSession, RequestCode.INTERNAL_QQ_LOGIN_RSP, builder.build());
				return null;
			});
		}
	}

	public void loadThenLogin(ChannelHandlerContext ioSession, String sessionId, String pwd, String ip, int playerId) {
	}

	public void onAccountLoginFail(ChannelHandlerContext ioSession, String sessionId, int code, String error) {
	}

	public void onAccountLoginSuccess(ChannelHandlerContext ioSession, UserData player, String sessionId, String ip) {
	}

}
