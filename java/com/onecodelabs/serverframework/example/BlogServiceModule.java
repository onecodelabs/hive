package com.onecodelabs.serverframework.example;

import com.google.inject.AbstractModule;
import com.onecodelabs.serverframework.example.actions.CreateBlogAction;
import com.onecodelabs.serverframework.example.actions.GetBlogAction;
import com.onecodelabs.serverframework.example.actions.ListBlogsAction;
import com.onecodelabs.serverframework.request.ActionModule;
import com.onecodelabs.serverframework.request.RpcDispatcherModule;

public class BlogServiceModule extends AbstractModule {

    @Override
    protected void configure() {
        install(RpcDispatcherModule.forService(BlogService.getDescriptor()));
        install(ActionModule.forActions(
                GetBlogAction.class,
                CreateBlogAction.class,
                ListBlogsAction.class
        ));
    }
}
