package com.onecodelabs.serverframework.example.actions;

import com.onecodelabs.serverframework.example.CreateBlogRequest;
import com.onecodelabs.serverframework.example.CreateBlogResponse;
import com.onecodelabs.serverframework.request.Action;
import com.onecodelabs.serverframework.request.RequestPath;

@RequestPath(path = "/BlogService.CreateBlog")
public class CreateBlogAction implements Action<CreateBlogRequest, CreateBlogResponse> {

    @Override
    public CreateBlogResponse handleRequest(CreateBlogRequest request) {
        return CreateBlogResponse.newBuilder()
                .setBlog(request.getBlog())
                .build();
    }
}
