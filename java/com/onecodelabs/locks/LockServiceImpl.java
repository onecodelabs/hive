package com.onecodelabs.locks;

import io.grpc.stub.StreamObserver;
import proto.locks.LockOuterClass;
import proto.locks.LockServiceGrpc;
import proto.locks.LockServiceOuterClass;

public class LockServiceImpl extends LockServiceGrpc.LockServiceImplBase {

    @Override
    public void acquire(LockServiceOuterClass.AcquireRequest request, StreamObserver<LockServiceOuterClass.AcquireResponse> responseObserver) {
        String name = request.getName();
        LockOuterClass.Lock lock = LockOuterClass.Lock.newBuilder().setName(name).build();
        LockServiceOuterClass.AcquireResponse response = LockServiceOuterClass.AcquireResponse.newBuilder().setLock(lock).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void release(LockServiceOuterClass.ReleaseRequest request, StreamObserver<LockServiceOuterClass.ReleaseResponse> responseObserver) {
        LockServiceOuterClass.ReleaseResponse response = LockServiceOuterClass.ReleaseResponse.newBuilder().build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void check(LockServiceOuterClass.CheckRequest request, StreamObserver<LockServiceOuterClass.CheckResponse> responseObserver) {
        LockServiceOuterClass.CheckResponse response = LockServiceOuterClass.CheckResponse.newBuilder().build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
