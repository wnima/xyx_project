package chat;

import com.game.domain.Player;

import data.bean.Lord;
import pb.CommonPb.ChatPB;

public class ManChat extends Chat {
	private Player player;
	private int time;
	private String msg;

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * Overriding: ser
	 * 
	 * @return
	 * @see com.game.chat.domain.Chat#ser()
	 */
	@Override
	public ChatPB ser(int style) {
		ChatPB.Builder builder = ChatPB.newBuilder();
		Lord lord = player.lord;
		builder.setTime(time);
		builder.setChannel(channel);
		builder.setName(lord.getNick());
		builder.setPortrait(lord.getPortrait());
		if (lord.getVip() > 0) {
			builder.setVip(lord.getVip());
		}
		
		if (style != 0) {
			builder.setStyle(style);
		}
		builder.setMsg(msg);
		if (player.account.getIsGm() > 0) {
			builder.setIsGm(true);
		}
		
		if (player.account.getIsGuider() > 0) {
			builder.setIsGuider(true);
		}
		
		builder.setStaffing(player.lord.getStaffing());
		
		return builder.build();
	}

}
