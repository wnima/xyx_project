package network.handler.module;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

import inject.BeanManager;
import network.AbstractHandlers;
import network.IModuleMessageHandler;

@Singleton
public class NoticeModule implements IModuleMessageHandler {

	private static Logger logger = LoggerFactory.getLogger(NoticeModule.class);

	public static NoticeModule getInst() {
		return BeanManager.getBean(NoticeModule.class);
	}

	@Override
	public void registerModuleHandler(AbstractHandlers handler) {
	}


}
