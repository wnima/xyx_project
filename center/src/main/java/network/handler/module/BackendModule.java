package network.handler.module;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import network.AbstractHandlers;
import network.IModuleMessageHandler;

public class BackendModule implements IModuleMessageHandler {
	private static Logger logger = LoggerFactory.getLogger(BackendModule.class);

	@Override
	public void registerModuleHandler(AbstractHandlers handler) {
	}


}
