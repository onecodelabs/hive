load("@rules_proto//proto:defs.bzl", "proto_library")
load("@rules_java//java:defs.bzl", "java_proto_library")
load("//database:schema.bzl", "schema_bundle")

proto_library(
    name = "schema_proto",
    srcs = ["schema.proto"],
    visibility = ["//visibility:public"],
)

java_proto_library(
    name = "schema_proto_java",
    visibility = ["//visibility:public"],
    deps = [":schema_proto"],
)

filegroup(
    name = "example_schema",
    srcs = ["example_schema.textproto"],
)

schema_bundle(
    name = "example_schema_bundle",
    bundle_output = "",
    proto_libs = [
        "//proto/example:user_proto",
    ],
    schema_input = "example_schema.textproto",
)
