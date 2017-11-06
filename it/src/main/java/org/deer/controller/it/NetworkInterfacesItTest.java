package org.deer.controller.it;


import com.google.common.collect.ImmutableList;
import java.io.IOException;
import java.util.List;
import org.deer.network.NetworkInfoServiceGrpc;
import org.deer.network.NetworkService;
import org.deer.network.service.NetworkInfoServiceImpl;
import org.deer.proto.server.ServiceRegistry;
import org.junit.Test;

public class NetworkInterfacesItTest extends ServerClientTest {

    @Test
    public void testReadNetworkInterfaces() throws IOException {
        final NetworkInfoServiceGrpc.NetworkInfoServiceBlockingStub service =
                NetworkInfoServiceGrpc.newBlockingStub(client.getChannel());

        final NetworkService.NetworkInfoResponse networkInfo =
                service.getNetworkInfo(NetworkService.NetworkInfoRequest.newBuilder().build());

        output(networkInfo.toString(), "network-interfaces");
    }

    @Test
    public void testReadAddressInfo() throws IOException {
        final NetworkInfoServiceGrpc.NetworkInfoServiceBlockingStub service =
                NetworkInfoServiceGrpc.newBlockingStub(client.getChannel());

        final NetworkService.AddressInfoResponse response =
                service.getAddressInfo(NetworkService.AddressInfoRequest.newBuilder()
                        .setAddress("192.168.2.1")
                        .build());
        output(response.toString(), "address-info");
    }

    @Override
    List<ServiceRegistry.ServiceRegistration> getServices() {
        return ImmutableList.of(new ServiceRegistry.ServiceRegistration(new NetworkInfoServiceImpl()));
    }
}
