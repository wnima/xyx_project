package util;

import java.util.List;
import java.util.function.Function;

import com.google.protobuf.MessageLite;

import network.MessageBean;
import protocol.s2c.ResponseCode;


public interface IBroadcast <T extends MessageBean>{

	 default void broadcastMessage(ResponseCode code, MessageLite message) {
		broadcastMessage(code.getValue(), message);
	}

	 default void broadcastMessage(ResponseCode code, MessageLite message, List<T> excludePlayers) {
		broadcastMessage(code.getValue(), message, excludePlayers);
	}

	default void broadcastMessage(ResponseCode code, Function<T, MessageLite> fun) {
		broadcastMessage(code.getValue(), fun, null);
	}

	default void broadcastMessage(ResponseCode code, Function<T, MessageLite> fun, List<T> excludePlayers) {
		broadcastMessage(code.getValue(), fun, excludePlayers);
	}

	 default void broadcastMessage(int code, MessageLite message) {
		broadcastMessage(code, message, null);
	}

	 default void broadcastMessage(int code, Function<T, MessageLite> fun) {
		broadcastMessage(code, fun, null);
	}


	List<T> getPlayerList();

	default void broadcastMessage(int code, Function<T, MessageLite> fun, List<T> excludePlayers) {
		if (excludePlayers == null)
			getPlayerList().forEach(e -> e.write(code, fun.apply(e)));
		else {
			getPlayerList().forEach(e -> {
				if (!excludePlayers.contains(e)) {
					e.write(code, fun.apply(e));
				}
			});
		}
	}

	 default void broadcastMessage(int code, MessageLite message, List<T> excludePlayers) {
		if (excludePlayers == null)
			getPlayerList().forEach(e -> e.write(code, message));
		else {
			getPlayerList().forEach(e -> {
				if (!excludePlayers.contains(e)) {
					e.write(code, message);
				}
			});
		}
	}
}
