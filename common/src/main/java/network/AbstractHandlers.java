package network;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageLite;

import define.AppId;
import io.netty.channel.ChannelHandlerContext;
import packet.CocoPacket;
import protocol.c2s.RequestCode;
import util.LogHelper;
import util.Pair;

public abstract class AbstractHandlers {
	private static final Logger logger = LoggerFactory.getLogger(AbstractHandlers.class);

	public class MessageHolder<MessageLite> {
		private MessageLite t;

		public MessageHolder(MessageLite t) {
			this.t = t;
		}

		public <T> T get() {
			return (T) t;
		}
	}

	public AbstractHandlers() {
		registerAction();
	}

	public interface IActionHandler {
		void doAction(ChannelHandlerContext client, CocoPacket packet, MessageHolder<MessageLite> message);
	}

	protected Map<Integer, Pair<MessageLite, IActionHandler>> actionHandlers = new HashMap<>();

	public void registerAction(int action, IActionHandler handler, MessageLite message) {
		actionHandlers.put(action, new Pair<>(message, handler));
	}

	public void registerAction(int action, IActionHandler handler) {
		registerAction(action, handler, null);
	}

	protected abstract void registerAction();

	public abstract void handPacket(ChannelHandlerContext client, CocoPacket packet);

	protected void handlerMessage(ChannelHandlerContext client, CocoPacket packet, RequestCode reqCode, Pair<MessageLite, IActionHandler> messageAndHandler) {
		IActionHandler handler = messageAndHandler.getRight();
		MessageLite protoType = messageAndHandler.getLeft();
		if (handler != null) {
			MessageLite message = null;
			try {
				message = protoType == null ? null : protoType.getParserForType().parseFrom(packet.getBytes());
			} catch (InvalidProtocolBufferException e) {
				LogHelper.ERROR.error(e.getMessage(), e);
			}
			handler.doAction(client, packet, new MessageHolder<>(message));
		}
	}

	protected abstract AppId getAppId();

	protected abstract NetClient getCenterClient();

	public void handleSessionInActive(ServerSession session) {
		//do nothing   not every one need to override ,  but logic
	}
}
