package inject;

import com.google.inject.AbstractModule;

import service.BaseApp;
import service.GateApp;

public class GateBeanModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(BaseApp.class).to(GateApp.class);
	}
}
