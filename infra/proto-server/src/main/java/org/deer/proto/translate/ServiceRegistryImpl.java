package org.deer.proto.translate;

import io.grpc.BindableService;
import java.util.LinkedList;
import java.util.List;

public class ServiceRegistryImpl implements ServiceRegistry {

    private final List<BindableService> services;

    public ServiceRegistryImpl() {
        services = new LinkedList<>();
    }

    @Override
    public void registerService(final BindableService service) {
        this.services.add(service);
    }

    @Override
    public List<BindableService> getRegisteredServices() {
        return services;
    }
}
