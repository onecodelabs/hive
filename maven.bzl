load("@rules_jvm_external//:defs.bzl", "maven_install")
load("@io_grpc_grpc_java//:repositories.bzl", "IO_GRPC_GRPC_JAVA_ARTIFACTS", "IO_GRPC_GRPC_JAVA_OVERRIDE_TARGETS")

def maven():
    maven_install(
        artifacts = [
            "com.google.auto.service:auto-service:1.0.1",
            "com.google.auto.service:auto-service-annotations:1.0.1",
        ] + IO_GRPC_GRPC_JAVA_ARTIFACTS,
        generate_compat_repositories = True,
        override_targets = IO_GRPC_GRPC_JAVA_OVERRIDE_TARGETS,
        repositories = [
            "https://maven.google.com",
            "https://repo1.maven.org/maven2",
        ],
    )
