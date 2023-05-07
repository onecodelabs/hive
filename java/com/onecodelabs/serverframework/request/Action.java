package com.onecodelabs.serverframework.request;

import com.google.protobuf.Message;

public interface Action<Req, Res> {
    Res handleRequest(Req request);
}
