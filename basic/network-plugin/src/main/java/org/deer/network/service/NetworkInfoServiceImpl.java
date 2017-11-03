package org.deer.network.service;

import io.grpc.stub.StreamObserver;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import org.deer.network.Network;
import org.deer.network.NetworkInfoServiceGrpc;
import org.deer.network.NetworkService;
import org.deer.network.NetworkService.NetworkInfoResponse;

public class NetworkInfoServiceImpl extends NetworkInfoServiceGrpc.NetworkInfoServiceImplBase {

    @Override
    public void getNetworkInfo(final NetworkService.NetworkInfoRequest request,
                               final StreamObserver<NetworkInfoResponse> responseObserver) {
        try {
            final NetworkInfoResponse.Builder responseBuilder = NetworkInfoResponse.newBuilder();
            final Network.NetworkInfo.Builder networkInfoBuilder = Network.NetworkInfo.newBuilder();

            Collections.list(NetworkInterface.getNetworkInterfaces())
                    .stream()
                    .map(iface -> Network.Interface.newBuilder()
                            .setIndex(iface.getIndex())
                            .setName(iface.getName())
                            .build())
                    .forEach(networkInfoBuilder::addInterfaces);
            responseObserver.onNext(responseBuilder.setNetworkInfo(networkInfoBuilder).build());
        } catch (SocketException e) {
            responseObserver.onError(e);
        } finally {
            responseObserver.onCompleted();
        }
    }
}
