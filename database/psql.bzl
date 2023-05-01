load("@rules_java//java:defs.bzl", "java_binary")

def psql_schema(name, input_bundle):
    java_binary(
        name = name,
        main_class = "com.onecodelabs.database.PostgresSchemaGenerator",
        runtime_deps = ["//java/com/onecodelabs/database:generators"],
        args = [
            "--input_bundle=" + input_bundle,
        ],
        data = [input_bundle],
    )
