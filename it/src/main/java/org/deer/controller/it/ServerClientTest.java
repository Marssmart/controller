package org.deer.controller.it;

import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.Executors;
import org.deer.proto.client.ProtoClient;
import org.deer.proto.server.ProtoServer;
import org.deer.proto.server.ServiceRegistry;
import org.deer.proto.server.ServiceRegistryImpl;
import org.junit.After;
import org.junit.Before;

public abstract class ServerClientTest {

    protected ProtoClient client;
    private ServiceRegistry serviceRegistry;
    private ProtoServer protoServer;
    private File testResultFolder;

    @Before
    public void init() throws Exception {
        serviceRegistry = new ServiceRegistryImpl();
        getServices().forEach(serviceRegistration -> serviceRegistry.registerService(serviceRegistration));
        protoServer = new ProtoServer(5678, serviceRegistry, Executors.newSingleThreadExecutor());
        client = new ProtoClient("localhost", 5678, Executors.newSingleThreadExecutor());
        protoServer.start();
        client.start();
        final Path path = Paths.get("target/it-test-results");
        testResultFolder = path.toFile();
        if (!testResultFolder.exists()) {
            testResultFolder.mkdirs();
        }
    }

    @After
    public void after() throws InterruptedException {
        client.stop();
        protoServer.stop();
    }

    public void output(final String output, final String subfolder) throws IOException {
        final Path resultPath = Paths.get(testResultFolder.getPath(), subfolder);
        Files.write(output, resultPath.toFile(), StandardCharsets.UTF_8);
    }

    abstract List<ServiceRegistry.ServiceRegistration> getServices();
}
