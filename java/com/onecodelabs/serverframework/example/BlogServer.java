package com.onecodelabs.serverframework.example;

import com.google.common.collect.ImmutableList;
import com.google.inject.Module;
import com.onecodelabs.serverframework.ModuleBasedServer;

public class BlogServer extends ModuleBasedServer {

    public static void main(String[] args) {
        main(args, new BlogServer());
    }

    @Override
    protected ImmutableList<Module> getModules() {
        return ImmutableList.of(new BlogServerModule());
    }
}
