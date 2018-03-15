package com.kag.moduleupdatemanager;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import org.openide.modules.ModuleInstall;

public class Installer extends ModuleInstall {

	private static final Logger LOGGER = Logger.getLogger(ModuleUpdateManager.class.getName());

	private ModuleUpdateManager moduleUpdateManager;
	private ScheduledExecutorService executor;

	@Override
	public void restored() {
		moduleUpdateManager = new ModuleUpdateManager();

		//Executor for periodically checking for updates
		executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(new UpdateChecker(), 0, 5, TimeUnit.SECONDS);
	}

	@Override
	public void close() {
		executor.shutdown();
	}

	private class UpdateChecker implements Runnable {

		@Override
		public void run() {
			LOGGER.info("Attempt to find new modules and updates");
			moduleUpdateManager.syncWithUpdateCenter();
		}
	}
}
