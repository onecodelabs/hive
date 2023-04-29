package com.onecodelabs.locks;

//import io.grpc.Server;
//import io.grpc.ServerBuilder;
//import proto.locks.LockServiceGrpc.LockServiceStub;

import java.io.IOException;
import java.util.logging.Logger;

public class LockServer {

    private static final Logger logger = Logger.getLogger(LockServer.class.getName());
    public static final int PORT = 8080;

    public static void main(String[] args) throws IOException, InterruptedException {
//        Server server = ServerBuilder.forPort(PORT).addService(new LockServiceImpl()).build();
//        server.start();
//        logger.info("Server started, listening on " + PORT);
//        server.awaitTermination();
//        logger.info("Server shut down");
    }
}
