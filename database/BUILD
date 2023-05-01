load("//database:schema.bzl", "schema_bundle")
load("//database:psql.bzl", "psql_schema")

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
