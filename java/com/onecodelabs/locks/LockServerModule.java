package com.onecodelabs.locks;

import com.google.inject.AbstractModule;
import com.google.inject.Binding;
import com.google.inject.Provides;
import com.google.inject.multibindings.Multibinder;
import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.util.Set;

public class LockServerModule extends AbstractModule {

    public static final int PORT = 8080;

    @Override
    protected void configure() {
        Multibinder<BindableService> multibinder1 = Multibinder.newSetBinder(binder(), BindableService.class);
        multibinder1.addBinding().to(LockServiceImpl.class);

        Multibinder<BindableService> multibinder2 = Multibinder.newSetBinder(binder(), BindableService.class);
        multibinder2.addBinding().to(FooServiceImpl.class);


//        bind(BindableService.class).toInstance(new LockServiceImpl());
//        bind(BindableService.class).toInstance(new FooServiceImpl());
//        bind(BindableService.class).to(LockServiceImpl.class);
    }

    @Provides
    Server provideServer(Set<BindableService> bindableServices) {
        ServerBuilder serverBuilder = ServerBuilder.forPort(PORT);
        for (BindableService bindableService : bindableServices) {
            serverBuilder.addService(bindableService);
        }
        return serverBuilder.build();
    }
}
