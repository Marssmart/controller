package org.deer.proto.translate;

import io.grpc.BindableService;
import java.util.List;

public interface ServiceRegistry {

    void registerService(BindableService var1);

    List<BindableService> getRegisteredServices();

}
