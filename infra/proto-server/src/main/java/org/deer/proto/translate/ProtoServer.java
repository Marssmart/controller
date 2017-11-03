package org.deer.proto.translate;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.util.concurrent.ExecutorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProtoServer {

    private static final Logger LOG = LoggerFactory.getLogger(ProtoServer.class);

    private final int port;
    private final ServiceRegistry serviceRegistry;
    private final ExecutorService executor;

    private Server server;

    public ProtoServer(final int port, final ServiceRegistry serviceRegistry, final ExecutorService executor) {
        this.port = port;
        this.serviceRegistry = serviceRegistry;
        this.executor = executor;
    }

    public void start() throws Exception {
        ServerBuilder<?> serverBuilder = ServerBuilder.forPort(port);
        LOG.info("Starting server on port {}", port);
        serviceRegistry.getRegisteredServices().stream()
                .peek(service -> LOG.info("Registering service {}", service))
                .forEach(serverBuilder::addService);
        server = serverBuilder
                .executor(executor)
                .build();
        server.start();
        LOG.info("Server successfully started");
    }

    public void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
    }

    public void stop() {
        if (server != null) {
            LOG.info("Shutting down proto server");
            server.shutdown();
            executor.shutdown();
            LOG.info("Proto server has been shut down");
        }
    }
}
