package com.onecodelabs.serverframework.example.actions;

import com.onecodelabs.serverframework.example.GetBlogRequest;
import com.onecodelabs.serverframework.example.GetBlogResponse;
import com.onecodelabs.serverframework.request.Action;
import com.onecodelabs.serverframework.request.RequestPath;

@RequestPath(path = "/BlogService.GetBlog")
public class GetBlogAction implements Action<GetBlogRequest, GetBlogResponse> {

    @Override
    public GetBlogResponse handleRequest(GetBlogRequest request) {
        GetBlogRequest.getDescriptor();
        return GetBlogResponse.getDefaultInstance();
    }
}
