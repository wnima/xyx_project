package chat;

import com.game.domain.Player;

import data.bean.Lord;
import pb.CommonPb;
import pb.CommonPb.ChatPB;

public class ManShare extends Chat {
	private Player player;
	private int time;
	private int id;
	private String[] param;
	private int report;
	private int sysId;
	private CommonPb.TankData tankData;
	private int heroId;

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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String[] getParam() {
		return param;
	}

	public void setParam(String[] param) {
		this.param = param;
	}

	public int getReport() {
		return report;
	}

	public void setReport(int report) {
		this.report = report;
	}

	/**
	 * Overriding: ser
	 * 
	 * @return
	 * @see com.game.chat.domain.Chat#ser()
	 */
	@Override
	public ChatPB ser(int style) {
		// TODO Auto-generated method stub
		ChatPB.Builder builder = ChatPB.newBuilder();
		Lord lord = player.lord;
		builder.setTime(time);
		builder.setChannel(channel);
		builder.setName(lord.getNick());
		builder.setPortrait(lord.getPortrait());
		if (lord.getVip() > 0) {
			builder.setVip(lord.getVip());
		}

		if (id != 0) {
			builder.setId(id);
		}

		if (param != null) {
			for (int i = 0; i < param.length; i++) {
				if (param[i] != null) {
					builder.addParam(param[i]);
				}
			}
		}

		if (report != 0) {
			builder.setReport(report);
		}

		if (tankData != null) {
			builder.setTankData(tankData);
		}

		if (sysId != 0) {
			builder.setSysId(sysId);
		}
		
		if (heroId != 0) {
			builder.setHeroId(heroId);
		}
		
		if (player.account.getIsGuider() > 0) {
			builder.setIsGuider(true);
		}
		
		builder.setStaffing(player.lord.getStaffing());

		return builder.build();
	}

	public CommonPb.TankData getTankData() {
		return tankData;
	}

	public void setTankData(CommonPb.TankData tankData) {
		this.tankData = tankData;
	}

	public int getSysId() {
		return sysId;
	}

	public void setSysId(int sysId) {
		this.sysId = sysId;
	}

	public int getHeroId() {
		return heroId;
	}

	public void setHeroId(int heroId) {
		this.heroId = heroId;
	}
}
