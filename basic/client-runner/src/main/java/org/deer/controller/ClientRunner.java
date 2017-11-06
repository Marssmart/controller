package org.deer.controller;

import java.util.concurrent.Executors;
import org.deer.proto.client.ProtoClient;

public class ClientRunner {

    public static void main(String[] args) throws InterruptedException {
        final String localhost = "localhost";
        final int port = 5444;
        final ProtoClient protoClient = new ProtoClient(localhost, port, Executors.newCachedThreadPool());
        protoClient.start();
        protoClient.stop();
    }
}
