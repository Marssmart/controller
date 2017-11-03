package org.deer.proto.server;

import java.util.LinkedList;
import java.util.List;

public class ServiceRegistryImpl implements ServiceRegistry {

    private final List<ServiceRegistration> services;

    public ServiceRegistryImpl() {
        services = new LinkedList<>();
    }

    @Override
    public void registerService(final ServiceRegistration service) {
        this.services.add(service);
    }

    @Override
    public List<ServiceRegistration> getRegisteredServices() {
        return services;
    }
}
