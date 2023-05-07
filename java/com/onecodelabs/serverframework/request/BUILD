package(
    default_visibility = ["//visibility:public"],
)

java_library(
    name = "modules",
    srcs = [
        "ActionModule.java",
        "RpcDispatcherModule.java",
    ],
    deps = [
        "//java/com/onecodelabs/common:utils",
        "//java/com/onecodelabs/serverframework/request",
        "@io_grpc_grpc_java//api",
        "@io_grpc_grpc_java//protobuf",
        "@io_grpc_grpc_java//stub",
        "@maven//:com_google_guava_guava",
        "@maven//:com_google_inject_guice",
        "@maven//:com_google_protobuf_protobuf_java",
    ],
)

java_library(
    name = "request",
    srcs = [
        "Action.java",
        "RequestPath.java",
        "Rpc.java",
    ],
    deps = ["@maven//:com_google_protobuf_protobuf_java"],
)
