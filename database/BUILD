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

schema_bundle(
    name = "example_schema",
    proto_libs = [
        "//proto/example:user_proto",
    ],
    schema_input = "example_schema.textproto",
)

psql_schema(
    name = "psql_example_schema",
    input_bundle = "//database:example_schema",
)
