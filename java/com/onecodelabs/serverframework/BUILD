package(
    default_visibility = ["//visibility:public"],
)

java_library(
    name = "modules",
    srcs = [
        "ServerFrameworkModule.java",
    ],
    deps = [
        "//java/com/onecodelabs/flags",
        "@io_grpc_grpc_java//api",
        "@io_grpc_grpc_java//stub",
        "@maven//:com_google_inject_guice",
    ],
)

java_library(
    name = "serverframework",
    srcs = [
        "ModuleBasedServer.java",
    ],
    visibility = ["//visibility:public"],
    runtime_deps = ["@io_grpc_grpc_java//netty"],
    deps = [
        ":modules",
        "//java/com/onecodelabs/common:utils",
        "//java/com/onecodelabs/flags",
        "@io_grpc_grpc_java//api",
        "@io_grpc_grpc_java//stub",
        "@maven//:com_google_guava_guava",
        "@maven//:com_google_inject_guice",
    ],
)
