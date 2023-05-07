load("@io_grpc_grpc_java//:java_grpc_library.bzl", "java_grpc_library")

package(
    default_visibility = ["//java/com/onecodelabs/serverframework/example:__subpackages__"],
)

proto_library(
    name = "blog_proto",
    srcs = ["blog.proto"],
)

proto_library(
    name = "blog_service_proto",
    srcs = ["blog_service.proto"],
    deps = [":blog_proto"],
)

java_proto_library(
    name = "blog_service_java_proto",
    deps = [":blog_service_proto"],
)

java_grpc_library(
    name = "blog_service_java_grpc",
    srcs = [":blog_service_proto"],
    deps = [":blog_service_java_proto"],
)

java_library(
    name = "modules",
    srcs = [
        "BlogServerModule.java",
        "BlogServiceModule.java",
    ],
    deps = [
        ":blog_service_java_proto",
        "//java/com/onecodelabs/serverframework/example/actions",
        "//java/com/onecodelabs/serverframework/request:modules",
        "@maven//:com_google_inject_guice",
    ],
)

java_library(
    name = "BlogServer_library",
    srcs = ["BlogServer.java"],
    deps = [
        ":modules",
        "//java/com/onecodelabs/serverframework",
        "@maven//:com_google_guava_guava",
        "@maven//:com_google_inject_guice",
    ],
)

java_binary(
    name = "BlogServer",
    main_class = "com.onecodelabs.serverframework.example.BlogServer",
    runtime_deps = [
        ":BlogServer_library",
    ],
)
