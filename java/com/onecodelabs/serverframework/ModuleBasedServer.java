package com.onecodelabs.serverframework;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.onecodelabs.flags.Flags;
import com.onecodelabs.serverframework.ServerFrameworkModule.ServerPort;
import io.grpc.Server;

import java.io.IOException;
import java.util.logging.Logger;

public abstract class ModuleBasedServer {

    private static final Logger logger = Logger.getLogger(ModuleBasedServer.class.getName());

    private static Injector INJECTOR;

    protected static void main(String[] args, ModuleBasedServer server) {
        Flags.parse(args);
        createInjector(server);
        startServer();
    }

    private static void createInjector(ModuleBasedServer server) {
        INJECTOR = Guice.createInjector(ImmutableList.copyOf(Iterables.concat(
                server.getModules(),
                ImmutableList.of(new ServerFrameworkModule())
        )));
    }

    private static void startServer() {
        Integer port = INJECTOR.getInstance(Key.get(Integer.class, ServerPort.class));
        Server grpcServer = INJECTOR.getInstance(Server.class);

        System.out.printf("Registered %d services\n", grpcServer.getServices().size());
        try {
            grpcServer.start();
            logger.info("Server started, listening on " + port);
            grpcServer.awaitTermination();
            logger.info("Server shut down");
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static Injector getInjector() {
        return INJECTOR;
    }

    protected abstract ImmutableList<Module> getModules();
}
