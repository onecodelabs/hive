package com.onecodelabs.serverframework.example.actions;

import com.onecodelabs.serverframework.example.ListBlogsRequest;
import com.onecodelabs.serverframework.example.ListBlogsResponse;
import com.onecodelabs.serverframework.request.Action;
import com.onecodelabs.serverframework.request.RequestPath;

@RequestPath(path = "/BlogService.ListBlogs")
public class ListBlogsAction implements Action<ListBlogsRequest, ListBlogsResponse> {

    @Override
    public ListBlogsResponse handleRequest(ListBlogsRequest request) {
        return ListBlogsResponse.getDefaultInstance();
    }
}
