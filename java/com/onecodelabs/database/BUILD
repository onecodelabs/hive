load("@rules_java//java:defs.bzl", "java_binary", "java_library")

java_library(
    name = "database_flags",
    srcs = ["DatabaseFlags.java"],
    deps = [
        "//java/com/onecodelabs/flags",
    ],
)

# Used in //database:schema.bzl
java_library(
    name = "SchemaConformanceTest",
    srcs = ["SchemaConformanceTest.java"],
    visibility = ["//visibility:public"],
    deps = [
        ":database_flags",
        "//database:schema_proto_java",
        "//java/com/onecodelabs/common:utils",
        "//java/com/onecodelabs/flags",
        "@maven//:com_google_protobuf_protobuf_java",
        "@maven//:junit_junit",
    ],
)

java_library(
    name = "generators",
    srcs = [
        "PostgresSchemaGenerator.java",
        "SchemaGenerator.java",
    ],
    visibility = ["//visibility:public"],
    deps = [
        ":database_flags",
        "//database:schema_proto_java",
        "//java/com/onecodelabs/common:utils",
        "//java/com/onecodelabs/flags",
        "@maven//:com_google_protobuf_protobuf_java",
    ],
)

java_library(
    name = "SchemaBundleGenerator",
    srcs = ["SchemaBundleGenerator.java"],
    visibility = ["//visibility:public"],
    deps = [
        ":database_flags",
        "//database:schema_proto_java",
        "//java/com/onecodelabs/common:utils",
        "//java/com/onecodelabs/flags",
        "@maven//:com_google_protobuf_protobuf_java",
    ],
)
