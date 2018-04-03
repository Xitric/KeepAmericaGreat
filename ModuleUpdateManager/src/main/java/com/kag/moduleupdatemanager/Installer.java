package com.kag.moduleupdatemanager;

import org.openide.modules.ModuleInstall;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Installer extends ModuleInstall {

	private static final Logger LOGGER = Logger.getLogger(ModuleUpdateManager.class.getName());

	private ModuleUpdateManager moduleUpdateManager;
	private ScheduledExecutorService executor;

	@Override
	public void restored() {
		moduleUpdateManager = new ModuleUpdateManager();

		//Executor for periodically checking for updates
		executor = Executors.newScheduledThreadPool(1, new DaemonThreadFactory());
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

	/**
	 * This thread factory is used to create threads for the executor that stop when the game quits.
	 */
	private class DaemonThreadFactory implements ThreadFactory {

		@Override
		public Thread newThread(Runnable runnable) {
			Thread t = Executors.defaultThreadFactory().newThread(runnable);
			t.setDaemon(true);
			return t;
		}
	}
}
