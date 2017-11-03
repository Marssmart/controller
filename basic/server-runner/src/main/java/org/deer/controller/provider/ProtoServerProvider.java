package org.deer.controller.provider;

import com.google.inject.Inject;
import com.google.inject.Provider;
import java.util.concurrent.Executors;
import org.deer.proto.translate.ProtoServer;
import org.deer.proto.translate.ServiceRegistry;

public class ProtoServerProvider implements Provider<ProtoServer> {

    @Inject
    private ServiceRegistry serviceRegistry;

    public ProtoServer get() {
        final ProtoServer protoServer = new ProtoServer(5444, serviceRegistry, Executors.newCachedThreadPool());

        protoServer.addShutdownHook();
        try {
            protoServer.start();
        } catch (Exception e) {
            throw new IllegalStateException("Unable to start server", e);
        }

        return protoServer;
    }
}
