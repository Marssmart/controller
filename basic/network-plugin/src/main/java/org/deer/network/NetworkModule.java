package org.deer.network;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import org.deer.network.service.NetworkInfoServiceImpl;
import org.deer.proto.server.ServiceRegistry;

public class NetworkModule extends AbstractModule {

    @Override
    protected void configure() {
        final Multibinder<ServiceRegistry.ServiceRegistration> serviceBinder =
                Multibinder.newSetBinder(binder(), ServiceRegistry.ServiceRegistration.class);

        serviceBinder.addBinding().toInstance(new ServiceRegistry.ServiceRegistration(new NetworkInfoServiceImpl()));
    }
}
