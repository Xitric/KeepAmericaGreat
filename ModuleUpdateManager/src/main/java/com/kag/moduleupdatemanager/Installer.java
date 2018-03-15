package com.kag.moduleupdatemanager;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.netbeans.api.autoupdate.InstallSupport;
import org.netbeans.api.autoupdate.OperationContainer;
import org.openide.modules.ModuleInstall;
import org.openide.util.Exceptions;

public class Installer extends ModuleInstall {

	private ModuleUpdateManager moduleUpdateManager;
	private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

	@Override
	public void restored() {
		executor.scheduleAtFixedRate(new Worker(), 5, 5, TimeUnit.SECONDS);
	}

	private class Worker implements Runnable {

		@Override
		public void run() {
			System.out.println("Attempt to find new modules and updates");
			moduleUpdateManager = new ModuleUpdateManager();
			moduleUpdateManager.searchNewAndUpdateModules();
			try {
				OperationContainer<InstallSupport> installContainer
						= moduleUpdateManager.addToContainer(OperationContainer.createForInstall(), moduleUpdateManager.getInstall());

				OperationContainer<InstallSupport> updateContainer
						= moduleUpdateManager.addToContainer(OperationContainer.createForUpdate(), moduleUpdateManager.getUpdate());

				moduleUpdateManager.installModules(installContainer);
				moduleUpdateManager.installModules(updateContainer);
			} catch (Exception ex) {
				Exceptions.printStackTrace(ex);
			}
		}
	}

	@Override
	public void close() {
		executor.shutdown();
	}
}
