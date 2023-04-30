load("@io_grpc_grpc_java//:java_grpc_library.bzl", "java_grpc_library")

proto_library(
    name = "lock_proto",
    srcs = ["lock.proto"],
)

java_proto_library(
    name = "lock_proto_java",
    visibility = ["//java/com/onecodelabs/locks:__pkg__"],
    deps = [":lock_proto"],
)

proto_library(
    name = "lock_service_proto",
    srcs = ["lock_service.proto"],
    deps = [":lock_proto"],
)

java_proto_library(
    name = "lock_service_proto_java",
    deps = [":lock_service_proto"],
)

java_grpc_library(
    name = "lock_service_java_grpc",
    srcs = [":lock_service_proto"],
    visibility = ["//java/com/onecodelabs/locks:__pkg__"],
    deps = [":lock_service_proto_java"],
)
