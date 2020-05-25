package inject;

import com.google.inject.AbstractModule;

import service.BaseApp;
import service.CenterServer;

public class CenterBeanModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(BaseApp.class).to(CenterServer.class);
	}
}
