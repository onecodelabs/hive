package com.onecodelabs.locks;

import io.grpc.stub.StreamObserver;

public class FooServiceImpl extends FooServiceGrpc.FooServiceImplBase {
    @Override
    public void acquire(AcquireRequest request, StreamObserver<AcquireResponse> responseObserver) {
        System.out.println(LockServiceGrpc.class);
        String name = request.getName();
        Lock lock = Lock.newBuilder().setName(name).build();
        AcquireResponse response = AcquireResponse.newBuilder().setLock(lock).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void release(ReleaseRequest request, StreamObserver<ReleaseResponse> responseObserver) {
        ReleaseResponse response = ReleaseResponse.newBuilder().build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void check(CheckRequest request, StreamObserver<CheckResponse> responseObserver) {
        CheckResponse response = CheckResponse.newBuilder().build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}