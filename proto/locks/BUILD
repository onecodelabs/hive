load("@rules_proto//proto:defs.bzl", "proto_library")
load("@rules_java//java:defs.bzl", "java_proto_library")
load("@rules_proto_grpc//java:defs.bzl", "java_grpc_library")

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

java_grpc_library(
    name = "lock_service_proto_java",
    protos = [":lock_service_proto"],
    visibility = ["//java/com/onecodelabs/locks:__pkg__"],
    deps = [":lock_proto_java"],
)
