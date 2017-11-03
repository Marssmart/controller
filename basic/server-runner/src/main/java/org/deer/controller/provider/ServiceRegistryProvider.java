package org.deer.controller.provider;

import com.google.inject.Inject;
import com.google.inject.Provider;
import java.util.Set;
import org.deer.proto.server.ServiceRegistry;
import org.deer.proto.server.ServiceRegistryImpl;

public class ServiceRegistryProvider implements Provider<ServiceRegistry> {

    @Inject
    private Set<ServiceRegistry.ServiceRegistration> serviceRegistrations;

    @Override
    public ServiceRegistry get() {
        final ServiceRegistry serviceRegistry = new ServiceRegistryImpl();
        serviceRegistrations.forEach(serviceRegistry::registerService);
        return serviceRegistry;
    }
}
