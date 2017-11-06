package org.deer.proto.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProtoClient {

    private static final Logger LOG = LoggerFactory.getLogger(ProtoClient.class);

    private final String host;
    private final int port;
    private final ExecutorService executorService;

    private ManagedChannel channel;

    public ProtoClient(final String host, final int port, final ExecutorService executorService) {
        this.host = host;
        this.port = port;
        this.executorService = executorService;
    }

    public ManagedChannel getChannel() {
        return channel;
    }

    public void start() {
        LOG.info("Starting client {}:{}", host, port);
        channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext(true)
                .executor(executorService)
                .build();
    }

    public void stop() throws InterruptedException {
        if (channel != null) {
            LOG.info("Stoping client for {}:{}", host, port);
            channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        }
    }
}
