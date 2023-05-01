load("@rules_java//java:defs.bzl", "java_library")

java_library(
    name = "utils",
    srcs = [
        "AssertUtils.java",
        "ProtoUtils.java",
        "ReflectionUtils.java",
    ],
    visibility = ["//visibility:public"],
    deps = [
        "@maven//:com_google_protobuf_protobuf_java",
    ],
)
