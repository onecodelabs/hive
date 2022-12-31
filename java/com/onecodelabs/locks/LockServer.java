package com.onecodelabs.locks;

import com.onecodelabs.common.flags.Flag;
import com.onecodelabs.common.flags.FlagSpec;
import com.onecodelabs.common.flags.Flags;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.logging.Logger;

public class LockServer {

    private static final Logger logger = Logger.getLogger(LockServer.class.getName());

    @FlagSpec(name = "lock_server_port", description = "Lock Server's port")
    private static final Flag<Integer> lockServerPort = Flag.of(8080);

    public static void main(String[] args) throws IOException, InterruptedException {
        Flags.parse(args);
        Server server = ServerBuilder.forPort(lockServerPort.get()).addService(new LockServiceImpl()).build();
        server.start();
        logger.info("Server started, listening on " + lockServerPort.get());
        server.awaitTermination();
        logger.info("Server shut down");
    }
}
