package org.deer.network.service;

import io.grpc.stub.StreamObserver;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.deer.network.Network;
import org.deer.network.NetworkInfoServiceGrpc;
import org.deer.network.NetworkService;
import org.deer.network.NetworkService.NetworkInfoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetworkInfoServiceImpl extends NetworkInfoServiceGrpc.NetworkInfoServiceImplBase {

    private static final Logger LOG = LoggerFactory.getLogger(NetworkInfoServiceImpl.class);

    private static Network.Interface buildInterface(final NetworkInterface iface) {
        LOG.debug("Build interface {}", iface.getName());
        final Network.Interface.Builder builder = Network.Interface.newBuilder();
        builder.setIndex(iface.getIndex())
                .setName(iface.getName())
                .setDisplayName(iface.getDisplayName())
                .setVirtual(iface.isVirtual())
                .setLoopback(isLoopback(iface))
                .setPointToPoint(isPointToPoint(iface))
                .addAllAddresses(() -> getInterfaceAddresses(iface).iterator())
                .addAllBindings(() -> getBindings(iface).iterator())
                .setUp(isUp(iface));

        if (iface.getParent() != null) {
            builder.setParent(buildInterface(iface.getParent()));
        }

        return builder.build();
    }

    private static List<Network.InterfaceAddress> getBindings(final NetworkInterface iface) {
        LOG.debug("Building bindings for interface {}", iface.getName());
        return iface.getInterfaceAddresses()
                .stream()
                .map(NetworkInfoServiceImpl::buildInterfaceAddresss)
                .collect(Collectors.toList());
    }

    private static Network.InterfaceAddress buildInterfaceAddresss(final InterfaceAddress ifaceAddress) {
        final Network.InterfaceAddress.Builder builder = Network.InterfaceAddress.newBuilder()
                .setAddress(ifaceAddress.getAddress().getHostAddress())
                .setMaskLength(ifaceAddress.getNetworkPrefixLength());
        if (ifaceAddress.getBroadcast() != null) {
            builder.setBroadcast(ifaceAddress.getBroadcast().getHostAddress());
        }
        return builder.build();
    }

    private static List<String> getInterfaceAddresses(final NetworkInterface iface) {
        LOG.debug("Building interface addresses for interface {}", iface.getName());
        return Collections.list(iface.getInetAddresses())
                .stream()
                .map(InetAddress::getHostAddress)
                .collect(Collectors.toList());
    }

    private static boolean isUp(final NetworkInterface iface) {
        try {
            return iface.isUp();
        } catch (SocketException e) {
            LOG.warn("Unable to resolver isUp for interface {}",
                    Optional.ofNullable(iface.getDisplayName()).orElse(iface.getName()));
            return false;
        }
    }

    private static boolean isPointToPoint(final NetworkInterface iface) {
        try {
            return iface.isPointToPoint();
        } catch (SocketException e) {
            LOG.warn("Unable to resolver isPointToPoint for interface {}",
                    Optional.ofNullable(iface.getDisplayName()).orElse(iface.getName()));
            return false;
        }
    }

    private static boolean isLoopback(final NetworkInterface iface) {
        try {
            return iface.isLoopback();
        } catch (SocketException e) {
            LOG.warn("Unable to resolver isLoopback for interface {}",
                    Optional.ofNullable(iface.getDisplayName()).orElse(iface.getName()));
            return false;
        }
    }

    @Override
    public void getNetworkInfo(final NetworkService.NetworkInfoRequest request,
                               final StreamObserver<NetworkInfoResponse> responseObserver) {
        try {
            final NetworkInfoResponse.Builder responseBuilder = NetworkInfoResponse.newBuilder();
            final Network.NetworkInfo.Builder networkInfoBuilder = Network.NetworkInfo.newBuilder();

            Collections.list(NetworkInterface.getNetworkInterfaces())
                    .stream()
                    .map(NetworkInfoServiceImpl::buildInterface)
                    .forEach(networkInfoBuilder::addInterfaces);
            responseObserver.onNext(responseBuilder.setNetworkInfo(networkInfoBuilder).build());
        } catch (Exception e) {
            responseObserver.onError(e);
        }
        responseObserver.onCompleted();
    }

    @Override
    public void getAddressInfo(final NetworkService.AddressInfoRequest request,
                               final StreamObserver<NetworkService.AddressInfoResponse> responseObserver) {
        try {
            final InetAddress address = InetAddress.getByName(request.getAddress());
            responseObserver.onNext(NetworkService.AddressInfoResponse.newBuilder()
                    .setAddress(Network.IpAddressInfo.newBuilder()
                            .setHostAddress(address.getHostAddress())
                            .setAnyLocalAddress(address.isAnyLocalAddress())
                            .setLinkLocal(address.isLinkLocalAddress())
                            .setMc(address.isMulticastAddress())
                            .setMcGlobal(address.isMCGlobal())
                            .setMcLinkLocal(address.isMCLinkLocal())
                            .setMcNodeLocal(address.isMCNodeLocal())
                            .setMcOrgLocal(address.isMCOrgLocal())
                            .setMcSiteLocal(address.isMCSiteLocal())
                            .build())
                    .build());
        } catch (UnknownHostException e) {
            responseObserver.onError(e);
        }
        responseObserver.onCompleted();
    }
}
