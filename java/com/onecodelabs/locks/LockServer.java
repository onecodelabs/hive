package com.onecodelabs.locks;

import com.google.inject.Binding;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class LockServer {

    private static final Logger logger = Logger.getLogger(LockServer.class.getName());

    public static void main(String[] args) throws IOException, InterruptedException {
        Injector injector = Guice.createInjector(new LockServerModule());

//        List<Binding<BindableService>> bindings = injector.findBindingsByType(TypeLiteral.get(BindableService.class));
        Server server = injector.getInstance(Server.class);

        System.out.println(server.getServices().size());
        server.start();
        logger.info("Server started, listening on " + 123123);
        server.awaitTermination();
        logger.info("Server shut down");
    }
}
