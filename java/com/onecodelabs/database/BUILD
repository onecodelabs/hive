java_library(
    name = "database_flags",
    srcs = ["DatabaseFlags.java"],
    visibility = ["//javatests/com/onecodelabs/database:__subpackages__"],
    deps = [
        "//java/com/onecodelabs/flags",
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
