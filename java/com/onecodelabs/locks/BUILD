java_library(
    name = "LockServer_library",
    srcs = [
        "FooServiceImpl.java",
        "LockServer.java",
        "LockServerModule.java",
        "LockServiceImpl.java",
    ],
    deps = [
        "//proto/locks:lock_proto_java",
        "//proto/locks:lock_service_java_grpc",
        "//proto/locks:lock_service_proto_java",
        "@io_grpc_grpc_java//api",
        "@io_grpc_grpc_java//stub",
        "@maven//:com_google_inject_guice",
    ],
)

java_binary(
    name = "LockServer",
    main_class = "com.onecodelabs.locks.LockServer",
    runtime_deps = [
        ":LockServer_library",
        "@io_grpc_grpc_java//netty",
    ],
)

java_library(
    name = "FooBar_library",
    srcs = ["FooBar.java"],
    deps = [
        #        "//proto/locks:lock_proto_java",
        #        "//proto/locks:lock_service_java_grpc",
        "//proto/locks:lock_service_proto_java",
        #        "@io_grpc_grpc_java//api",
        #        "@io_grpc_grpc_java//stub",
    ],
)

java_binary(
    name = "FooBar",
    main_class = "com.onecodelabs.locks.FooBar",
    runtime_deps = [
        ":FooBar_library",
        #        "@io_grpc_grpc_java//netty",
    ],
)
