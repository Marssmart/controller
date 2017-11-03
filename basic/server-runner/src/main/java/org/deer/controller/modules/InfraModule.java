package org.deer.controller.modules;

import com.google.inject.PrivateModule;
import com.google.inject.Singleton;
import org.deer.controller.provider.ProtoServerProvider;
import org.deer.controller.provider.ServiceRegistryProvider;
import org.deer.proto.server.ProtoServer;
import org.deer.proto.server.ServiceRegistry;

public class InfraModule extends PrivateModule {

    protected void configure() {
        bind(ServiceRegistry.class).toProvider(ServiceRegistryProvider.class).in(Singleton.class);
        bind(ProtoServer.class).toProvider(ProtoServerProvider.class).asEagerSingleton();
        expose(ProtoServer.class);
    }
}
