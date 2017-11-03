package org.deer.controller.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import org.deer.controller.provider.ProtoServerProvider;
import org.deer.proto.translate.ProtoServer;
import org.deer.proto.translate.ServiceRegistry;
import org.deer.proto.translate.ServiceRegistryImpl;

public class InfraModule extends AbstractModule {

    protected void configure() {
        bind(ServiceRegistry.class).to(ServiceRegistryImpl.class).in(Singleton.class);
        bind(ProtoServer.class).toProvider(ProtoServerProvider.class).asEagerSingleton();
    }
}
