package com.onecodelabs.serverframework;

import com.google.inject.AbstractModule;
import com.google.inject.BindingAnnotation;
import com.google.inject.Provides;
import com.google.inject.multibindings.Multibinder;
import com.onecodelabs.flags.Flag;
import com.onecodelabs.flags.FlagSpec;
import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Set;

public class ServerFrameworkModule extends AbstractModule {

    @FlagSpec(name = "server_port", description = "")
    public static Flag<Integer> serverPort = Flag.of(8080);

    @Provides
    @ServerPort
    Integer provideServerPort() {
        return serverPort.getNotNull();
    }

    @Provides
    Server provideServer(@ServerPort Integer serverPort, Set<BindableService> services) {
        ServerBuilder<?> serverBuilder = ServerBuilder.forPort(serverPort);
        for (BindableService service : services) {
            service.bindService().getMethods().stream().map(m -> m.getMethodDescriptor().getFullMethodName())
                    .forEach(System.out::println);
            serverBuilder.addService(service);
        }
        return serverBuilder.build();
    }

    @Override
    protected void configure() {
        Multibinder.newSetBinder(binder(), BindableService.class);
    }

    @Retention(RetentionPolicy.RUNTIME)
    @BindingAnnotation
    public @interface ServerPort {
    }
}
