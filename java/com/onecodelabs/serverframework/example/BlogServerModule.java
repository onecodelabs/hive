package com.onecodelabs.serverframework.example;

import com.google.inject.AbstractModule;

public class BlogServerModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new BlogServiceModule());
    }
}
