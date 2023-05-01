load("//database:schema.bzl", "schema_bundle")
load("//javatests/com/onecodelabs/database:schema.bzl", "schema_bundle_test")

# Used in //database:schema.bzl
java_library(
    name = "SchemaConformanceTest",
    srcs = ["SchemaConformanceTest.java"],
    visibility = ["//visibility:public"],
    deps = [
        "//database:schema_proto_java",
        "//java/com/onecodelabs/common:utils",
        "//java/com/onecodelabs/database:database_flags",
        "//java/com/onecodelabs/flags",
        "@maven//:com_google_protobuf_protobuf_java",
        "@maven//:junit_junit",
    ],
)

# Used in //javatests/com/onecodelabs/database:schema.bzl
java_library(
    name = "SchemaBundleGeneratorTest",
    srcs = ["SchemaBundleGeneratorTest.java"],
    deps = [
        "//database:schema_proto_java",
        "//java/com/onecodelabs/flags",
        "@maven//:com_google_protobuf_protobuf_java",
        "@maven//:junit_junit",
    ],
)

proto_library(
    name = "test_proto",
    srcs = ["test.proto"],
)

schema_bundle(
    name = "test_1_bundle",
    proto_libs = [":test_proto"],
    schema_input = "test_1_schema.textproto",
)

schema_bundle_test(
    name = "test_1",
    bundle_metadata = "test_1_bundle_metadata.textproto",
    schema_bundle = "//javatests/com/onecodelabs/database:test_1_bundle",
)
