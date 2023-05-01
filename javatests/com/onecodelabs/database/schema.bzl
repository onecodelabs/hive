load("@rules_java//java:defs.bzl", "java_test")
load("//bzl:common.bzl", "absolute_target")

def schema_bundle_test(name, schema_bundle, bundle_metadata):
    java_test(
        name = name,
        test_class = "com.onecodelabs.database.SchemaBundleGeneratorTest",
        runtime_deps = ["//javatests/com/onecodelabs/database:SchemaBundleGeneratorTest"],
        data = [schema_bundle, bundle_metadata],
        jvm_flags = [
            #            TODO: fix
            "-Dschema_bundle=" + _schema_bundle_path(absolute_target(schema_bundle)),
            "-Dbundle_metadata=" + native.package_name() + "/" + bundle_metadata,
        ],
    )

def _schema_bundle_path(label):
    if label.startswith("//"):
        return label.replace("//", "").replace(":", "/") + ".bundle"
    return label
