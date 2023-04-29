load("@rules_proto//proto:defs.bzl", "proto_library")
load("@rules_java//java:defs.bzl", "java_proto_library")

proto_library(
    name = "user_proto",
    srcs = ["user.proto"],
    visibility = ["//visibility:public"],
)

java_proto_library(
    name = "user_proto_java",
    visibility = ["//java/com/onecodelabs/helloworld:__pkg__"],
    deps = [":user_proto"],
)
