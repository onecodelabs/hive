load("@rules_java//java:defs.bzl", "java_binary", "java_library", "java_plugin", "java_proto_library", "java_test")

def define_java_proto_libs(name, proto_libs):
    lib_targets = []
    for proto_lib in proto_libs:
        lib_name = name + "." + proto_lib.replace("//", "").replace("/", "_").replace(":", "_") + "." + "java_proto_library"
        java_proto_library(
            name = lib_name,
            deps = [proto_lib],
        )
        lib_targets.append("//" + native.package_name() + ":" + lib_name)
    return lib_targets

def define_conformance_test(name, schema_input, java_proto_targets):
    java_test(
        name = name,
        size = "small",
        test_class = "com.onecodelabs.database.SchemaConformanceTest",
        runtime_deps = ["//java/com/onecodelabs/database:SchemaConformanceTest"] + java_proto_targets,
        jvm_flags = [
            "-Dfile_path=" + native.package_name() + "/" + schema_input,
        ],
        data = [schema_input],
    )

def define_postgres_generator(name, schema_input):
    java_binary(
        name = name,
        main_class = "com.onecodelabs.database.PostgresSchemaGenerator",
        runtime_deps = ["//java/com/onecodelabs/database:generators"],
        args = [
            "--file_path=" + native.package_name() + "/" + schema_input,
        ],
        data = [schema_input],
    )

def schema_bundle(name, schema_input, proto_libs, bundle_output):
    java_proto_targets = define_java_proto_libs(name, proto_libs)
    define_conformance_test(name + ".conformance", schema_input, java_proto_targets)
    define_postgres_generator(name + ".psql", schema_input)
