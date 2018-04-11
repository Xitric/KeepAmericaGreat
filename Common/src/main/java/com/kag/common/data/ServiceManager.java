package com.kag.common.data;

import org.openide.util.Lookup;
import org.openide.util.LookupListener;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * A class for maintaining a list of service providers in memory. Instances of this class will provide callbacks when
 * service providers appear or disappear.
 *
 * @param <T> the type of service to manage
 * @author Kasper
 */
public class ServiceManager<T> {

	private Class<T> serviceProviderClass;
	private List<T> serviceProviders;
	private Lookup.Result<T> lookupResult;
	private Consumer<T> additionCallback;
	private Consumer<T> deletionCallback;

	private final LookupListener lookupListener = ev -> {
		System.out.println("Lookup listener for " + serviceProviderClass.getName() + " called");
		Collection<? extends T> actualServiceProviders = lookupResult.allInstances();

		for (T serviceProvider : actualServiceProviders) {
			//Newly installed service provider
			if (!serviceProviders.contains(serviceProvider)) {
				serviceProviders.add(serviceProvider);
				System.out.println("Found new service: " + serviceProvider.getClass().getName());
				if (additionCallback != null) additionCallback.accept(serviceProvider);
			}
		}

		for (T serviceProvider : serviceProviders) {
			//Removed service provider
			if (!actualServiceProviders.contains(serviceProvider)) {
				serviceProviders.remove(serviceProvider);
				System.out.println("Service removed: " + serviceProvider.getClass().getName());
				if (deletionCallback != null) deletionCallback.accept(serviceProvider);
			}
		}
	};

	public ServiceManager(Class<T> serviceProviderClass) {
		this(serviceProviderClass, null, null);
	}

	public ServiceManager(Class<T> serviceProviderClass, Consumer<T> additionCallback, Consumer<T> deletionCallback) {
		this.serviceProviderClass = serviceProviderClass;
		this.additionCallback = additionCallback;
		this.deletionCallback = deletionCallback;

		serviceProviders = new CopyOnWriteArrayList<>();
		lookupResult = Lookup.getDefault().lookupResult(serviceProviderClass);
		lookupResult.addLookupListener(lookupListener);
		lookupResult.allItems();

		for (T serviceProvider : lookupResult.allInstances()) {
			serviceProviders.add(serviceProvider);
			System.out.println("Found existing service: " + serviceProvider.getClass().getName());
			if (additionCallback != null) additionCallback.accept(serviceProvider);
		}

	}

	public Collection<? extends T> getServiceProviders() {
		return lookupResult.allInstances();
	}
}
