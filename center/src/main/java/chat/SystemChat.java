package chat;

import pb.CommonPb.ChatPB;
public class SystemChat extends Chat {
	private int time;
	private int sysId;
	private String[] param;

	public int getSysId() {
		return sysId;
	}

	public void setSysId(int sysId) {
		this.sysId = sysId;
	}

	public String[] getParam() {
		return param;
	}

	public void setParam(String[] param) {
		this.param = param;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
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
		builder.setTime(time);
		builder.setChannel(channel);

		if (style != 0) {
			builder.setStyle(style);
		}

		if (param != null) {
			for (int i = 0; i < param.length; i++) {
				if (param[i] != null) {
					builder.addParam(param[i]);
				}
			}
		}

		builder.setSysId(sysId);
		return builder.build();
	}

}
