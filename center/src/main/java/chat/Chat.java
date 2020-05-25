package chat;

import pb.CommonPb.ChatPB;

public abstract class Chat {
	static final public int WORLD_CHANNEL = 1;
	static final public int PARTY_CHANNEL = 2;
	static final public int GM_CHANNEL = 3;
	static final public int PRIVATE_CHANNEL = 4;

	protected int channel;

	abstract public ChatPB ser(int style);

	public int getChannel() {
		return channel;
	}

	public void setChannel(int channel) {
		this.channel = channel;
	}
}
