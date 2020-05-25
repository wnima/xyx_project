package manager;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.inject.Singleton;

import chat.Chat;
import inject.BeanManager;
import pb.CommonPb.ChatPB;

@Singleton
public class ChatDataManager {
	static final int MAX_CHAT_COUNT = 15;

	private LinkedList<ChatPB> world = new LinkedList<>();

	private Map<Integer, LinkedList<ChatPB>> party = new HashMap<Integer, LinkedList<ChatPB>>();

	public static ChatDataManager getInst() {
		return BeanManager.getBean(ChatDataManager.class);
	}

	public ChatPB addWorldChat(Chat chat) {
		chat.setChannel(Chat.WORLD_CHANNEL);
		ChatPB b = chat.ser(0);
		world.add(b);
		if (world.size() > MAX_CHAT_COUNT) {
			world.removeFirst();
		}
		return b;
	}

	public ChatPB addHornChat(Chat chat, int style) {
		chat.setChannel(Chat.WORLD_CHANNEL);
		ChatPB b = chat.ser(style);
		world.add(b);
		if (world.size() > MAX_CHAT_COUNT) {
			world.removeFirst();
		}
		return b;
	}

	public ChatPB createPrivateChat(Chat chat) {
		chat.setChannel(Chat.PRIVATE_CHANNEL);
		ChatPB b = chat.ser(0);
		return b;
	}

	public ChatPB addPartyChat(Chat chat, int partyId) {
		chat.setChannel(Chat.PARTY_CHANNEL);

		LinkedList<ChatPB> list = party.get(partyId);
		if (list == null) {
			list = new LinkedList<>();
			party.put(partyId, list);
		}
		ChatPB b = chat.ser(0);
		list.add(b);

		if (list.size() > MAX_CHAT_COUNT) {
			list.removeFirst();
		}
		return b;
	}

	public List<ChatPB> getWorldChat() {
		return world;
	}

	public List<ChatPB> getPartyChat(int partyId) {
		return party.get(partyId);
	}

}
