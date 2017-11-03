package org.deer.controller;

import java.util.concurrent.Executors;
import org.deer.network.NetworkInfoServiceGrpc;
import org.deer.network.NetworkService;
import org.deer.proto.client.ProtoClient;

public class ClientRunner {

    public static void main(String[] args) throws InterruptedException {
        final String localhost = "localhost";
        final int port = 5444;
        final ProtoClient protoClient = new ProtoClient(localhost, port, Executors.newCachedThreadPool());
        protoClient.start();

        final NetworkInfoServiceGrpc.NetworkInfoServiceBlockingStub blockingService =
                NetworkInfoServiceGrpc.newBlockingStub(protoClient.getChannel());

        final NetworkService.NetworkInfoResponse networkInfo =
                blockingService.getNetworkInfo(NetworkService.NetworkInfoRequest.newBuilder().build());

        System.out.println("Network info found " + networkInfo);

        protoClient.stop();
    }
}
