package network.handler.module.red;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

import inject.BeanManager;
import network.AbstractHandlers;
import network.IModuleMessageHandler;
import network.handler.module.AccountModule;

@Singleton
public class MainSceneModule implements IModuleMessageHandler {

	private static Logger logger = LoggerFactory.getLogger(MainSceneModule.class);

	public static MainSceneModule getInst() {
		return BeanManager.getBean(MainSceneModule.class);
	}

	@Override
	public void registerModuleHandler(AbstractHandlers handler) {
	}

}
