package network.handler.module.tank;
//package com.game.module;
//
//import java.util.Iterator;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.game.constant.GameError;
//import com.game.domain.Guy;
//import com.game.domain.Player;
//import com.game.domain.p.Lord;
//import com.game.manager.PlayerDataManager;
//import com.game.message.handler.ClientHandler;
//import com.game.pb.GamePb.TipGuyRq;
//import com.game.pb.GamePb.TipGuyRs;
//import com.game.server.GameServer;
//import com.game.util.LogHelper;
//import com.game.util.TimeHelper;
//
///**
// * @author ChenKui
// * @version 创建时间：2016-4-25 上午9:43:08
// * @declare
// */
//@Service
//public class TipGuyService {
//
//	@Autowired
//	private PlayerDataManager playerDataManager;
//
//	public void tipGuyRq(TipGuyRq req, ClientHandler handler) {
//		Player player = playerDataManager.getPlayer(handler.getRoleId());
//		if (player == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		Lord lord = player.lord;
//		if (lord == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		long guyId = req.getLordId();
//		Player guyPlayer = playerDataManager.getPlayer(guyId);
//		if (guyPlayer == null) {
//			handler.sendErrorMsgToPlayer(GameError.PLAYER_NOT_EXIST);
//			return;
//		}
//		Guy guy = playerDataManager.getGuy(guyId);
//
//		guy.setVip(guyPlayer.lord.getVip());
//		guy.setLevel(guyPlayer.lord.getLevel());
//
//		TipGuyRs.Builder builder = TipGuyRs.newBuilder();
//		String content = guy.getContent();
//		if (content == null || content.equals("")) {
//			guy.setCount(guy.getCount() + 1);
//			guy.setFlag(true);
//			if (req.getChatMsg() != null) {
//				guy.setContent(req.getChatMsg());
//			}
//		} else {
//			guy.setCount(guy.getCount() + 1);
//			guy.setFlag(true);
//			if (req.getChatMsg() != null) {
//				String[] contents = content.split("\\|");
//				if (contents.length < 5) {
//					content = content + "|" + req.getChatMsg();
//				}
//				guy.setContent(content);
//			}
//		}
//		builder.setResult(true);
//		handler.sendMsgToPlayer(TipGuyRs.ext, builder.build());
//	}
//
//	/**
//	 * 
//	 * Method: saveTimerLogic
//	 * 
//	 * @Description: 定时保存举报
//	 * @return void
//	 * @throws
//	 */
//	public void saveTimerLogic() {
//		Iterator<Guy> iterator = playerDataManager.getGuyMap().values().iterator();
//		int now = TimeHelper.getCurrentSecond();
//		int saveCount = 0;
//		while (iterator.hasNext()) {
//			Guy guy = iterator.next();
//			if (guy.isFlag() && (now - guy.getLastSaveTime()) >= 300) {
//				GameServer.getInstance().saveGuyServer.saveData(guy.copyData());
//				guy.setFlag(false);
//				saveCount++;
//			}
//		}
//
//		if (saveCount != 0) {
//			LogHelper.SAVE_LOGGER.trace("save player count:" + saveCount);
//		}
//	}
//}
