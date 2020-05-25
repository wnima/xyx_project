package network.handler.module;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

import inject.BeanManager;
import network.AbstractHandlers;
import network.IModuleMessageHandler;

@Singleton
public class LobbyModule implements IModuleMessageHandler {
	private static Logger logger = LoggerFactory.getLogger(LobbyModule.class);

	public static LobbyModule getInst() {
		return BeanManager.getBean(LobbyModule.class);
	}

	@Override
	public void registerModuleHandler(AbstractHandlers handler) {

	}



}