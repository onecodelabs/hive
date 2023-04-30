load("//deps:fetch.bzl", "fetch_all_repositories")

fetch_all_repositories()

load("@io_grpc_grpc_java//:repositories.bzl", "IO_GRPC_GRPC_JAVA_ARTIFACTS", "IO_GRPC_GRPC_JAVA_OVERRIDE_TARGETS", "grpc_java_repositories")

grpc_java_repositories()

load("@com_google_protobuf//:protobuf_deps.bzl", "PROTOBUF_MAVEN_ARTIFACTS", "protobuf_deps")

protobuf_deps()

load("//deps:maven.bzl", "setup_maven")

setup_maven(
    IO_GRPC_GRPC_JAVA_ARTIFACTS + PROTOBUF_MAVEN_ARTIFACTS,
    IO_GRPC_GRPC_JAVA_OVERRIDE_TARGETS,
)

load("@maven//:compat.bzl", "compat_repositories")

compat_repositories()
