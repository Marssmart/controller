package org.deer.proto.server;

import io.grpc.BindableService;
import java.util.List;

public interface ServiceRegistry {

    void registerService(ServiceRegistration var1);

    List<ServiceRegistration> getRegisteredServices();

    class ServiceRegistration{
        private final BindableService service;

        public ServiceRegistration(final BindableService service) {
            this.service = service;
        }

        public BindableService getService() {
            return service;
        }

        @Override
        public String toString() {
            return "ServiceRegistration{" +
                    "service=" + service +
                    '}';
        }
    }
}
