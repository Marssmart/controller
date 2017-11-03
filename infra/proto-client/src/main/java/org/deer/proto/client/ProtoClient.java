package org.deer.proto.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class ProtoClient {

    private final String host;
    private final int port;
    private final ExecutorService executorService;

    private ManagedChannel channel;

    public ManagedChannel getChannel() {
        return channel;
    }

    public ProtoClient(final String host, final int port, final ExecutorService executorService) {
        this.host = host;
        this.port = port;
        this.executorService = executorService;
    }

    public void start() {
        channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext(true)
                .executor(executorService)
                .build();
    }

    public void stop() throws InterruptedException {
        if (channel != null) {
            channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        }
    }
}
