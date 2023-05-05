load("@rules_java//java:defs.bzl", "java_binary", "java_library", "java_plugin", "java_proto_library", "java_test")
load("//bzl:common.bzl", "absolute_target", "path2target")

def define_conformance_test(name, schema_input, proto_libs):
    proto_libs_list = ""
    separator = ""
    for proto_lib in proto_libs:
        proto_lib_path = absolute_target(proto_lib).replace("//", "").replace(":", "/") + "-descriptor-set.proto.bin"
        proto_libs_list = proto_libs_list + separator + proto_lib_path
        separator = ","

    java_test(
        name = name,
        size = "small",
        test_class = "com.onecodelabs.database.SchemaConformanceTest",
        runtime_deps = ["//javatests/com/onecodelabs/database:SchemaConformanceTest"],
        jvm_flags = [
            "-Dschema_path=" + native.package_name() + "/" + schema_input,
            "-Dproto_libs=" + proto_libs_list,
        ],
        data = [schema_input] + proto_libs,
    )

def define_bundle_generator(name, schema_input, proto_libs):
    schema_path = native.package_name() + "/" + schema_input
    java_binary(
        name = name + "_java_binary",
        main_class = "com.onecodelabs.database.SchemaBundleGenerator",
        runtime_deps = ["//java/com/onecodelabs/database:SchemaBundleGenerator"],
        data = [schema_input] + proto_libs,
    )
    target_name = "//" + native.package_name() + ":" + name + "_java_binary"

    proto_libs_list = ""
    separator = ""
    for proto_lib in proto_libs:
        proto_libs_list = proto_libs_list + separator + ("$(location %s)" % proto_lib)
        separator = ","

    native.genrule(
        name = name,
        outs = [name + ".bundle"],
        srcs = [schema_input] + proto_libs,
        tools = [target_name],
        cmd = """
                ./$(location %s) \
                --schema_path=$(location %s) \
                --proto_libs=%s \
                > "$@"
                """ % (target_name, path2target(schema_input), proto_libs_list),
    )

def schema_bundle(name, schema_input, proto_libs):
    define_conformance_test(name + ".conformance", schema_input, proto_libs)
    define_bundle_generator(name, schema_input, proto_libs)
