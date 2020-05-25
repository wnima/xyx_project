package network.handler.module;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

import inject.BeanManager;
import network.AbstractHandlers;
import network.IModuleMessageHandler;

@Singleton
public class MailModule implements IModuleMessageHandler {

	private static final Logger logger = LoggerFactory.getLogger(MailModule.class);

	public static MailModule getInst() {
		return BeanManager.getBean(MailModule.class);
	}

	@Override
	public void registerModuleHandler(AbstractHandlers handler) {
	}
}
