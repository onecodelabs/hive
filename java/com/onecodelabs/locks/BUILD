#load("@rules_java//java:defs.bzl", "java_binary", "java_library")

java_library(
    name = "locks_library",
    srcs = [
        "LockServer.java",
        "LockServiceImpl.java",
    ],
    deps = [
        "//proto/locks:lock_proto_java",
        "//proto/locks:lock_service_java_grpc",
        "//proto/locks:lock_service_proto_java",
        "@io_grpc_grpc_java//api",
        "@io_grpc_grpc_java//stub",
    ],
)

java_binary(
    name = "locks",
    main_class = "com.onecodelabs.locks.LockServer",
    runtime_deps = [
        ":locks_library",
        "@io_grpc_grpc_java//netty",
    ],
)
