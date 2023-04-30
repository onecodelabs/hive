load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

IO_GRPC_JAVA_EXTERNAL_TAG = "1.51.1"

IO_GRPC_JAVA_SHA = "75e5994ca18ae3ffaf67a2f08d0274215916b0ff56d62e9e5b447095e622714b"

http_archive(
    name = "io_grpc_grpc_java",
    sha256 = IO_GRPC_JAVA_SHA,
    strip_prefix = "grpc-java-%s" % IO_GRPC_JAVA_EXTERNAL_TAG,
    url = "https://github.com/grpc/grpc-java/archive/v%s.zip" % IO_GRPC_JAVA_EXTERNAL_TAG,
)

RULES_JVM_EXTERNAL_TAG = "4.5"

RULES_JVM_EXTERNAL_SHA = "b17d7388feb9bfa7f2fa09031b32707df529f26c91ab9e5d909eb1676badd9a6"

http_archive(
    name = "rules_jvm_external",
    sha256 = RULES_JVM_EXTERNAL_SHA,
    strip_prefix = "rules_jvm_external-%s" % RULES_JVM_EXTERNAL_TAG,
    url = "https://github.com/bazelbuild/rules_jvm_external/archive/%s.zip" % RULES_JVM_EXTERNAL_TAG,
)

load("@rules_jvm_external//:defs.bzl", "maven_install")
load("@io_grpc_grpc_java//:repositories.bzl", "IO_GRPC_GRPC_JAVA_ARTIFACTS")
load("@io_grpc_grpc_java//:repositories.bzl", "IO_GRPC_GRPC_JAVA_OVERRIDE_TARGETS")
load("@io_grpc_grpc_java//:repositories.bzl", "grpc_java_repositories")

grpc_java_repositories()

load("@com_google_protobuf//:protobuf_deps.bzl", "PROTOBUF_MAVEN_ARTIFACTS")
load("@com_google_protobuf//:protobuf_deps.bzl", "protobuf_deps")

protobuf_deps()

maven_install(
    artifacts = IO_GRPC_GRPC_JAVA_ARTIFACTS + PROTOBUF_MAVEN_ARTIFACTS,
    generate_compat_repositories = True,
    override_targets = IO_GRPC_GRPC_JAVA_OVERRIDE_TARGETS,
    repositories = [
        "https://repo.maven.apache.org/maven2/",
    ],
    strict_visibility = True,
)

load("@maven//:compat.bzl", "compat_repositories")

compat_repositories()
#load("//deps:fetch.bzl", "fetch_all_repositories")
#
#fetch_all_repositories()
#
#load("@rules_jvm_external//:defs.bzl", "maven_install")
#load("@io_grpc_grpc_java//:repositories.bzl", "IO_GRPC_GRPC_JAVA_ARTIFACTS")
#load("@io_grpc_grpc_java//:repositories.bzl", "IO_GRPC_GRPC_JAVA_OVERRIDE_TARGETS")
#load("@io_grpc_grpc_java//:repositories.bzl", "grpc_java_repositories")
#
#grpc_java_repositories()
#
#load("@com_google_protobuf//:protobuf_deps.bzl", "PROTOBUF_MAVEN_ARTIFACTS")
#load("@com_google_protobuf//:protobuf_deps.bzl", "protobuf_deps")
#
#protobuf_deps()
#
#maven_install(
#    artifacts = IO_GRPC_GRPC_JAVA_ARTIFACTS + PROTOBUF_MAVEN_ARTIFACTS,
#    generate_compat_repositories = True,
#    override_targets = IO_GRPC_GRPC_JAVA_OVERRIDE_TARGETS,
#    repositories = [
#        "https://repo.maven.apache.org/maven2/",
#    ],
#    strict_visibility = True,
#)
#
#load("@maven//:compat.bzl", "compat_repositories")
#
#compat_repositories()

#load("@rules_jvm_external//:repositories.bzl", "rules_jvm_external_deps")
#
#rules_jvm_external_deps()
#
#load("@rules_jvm_external//:setup.bzl", "rules_jvm_external_setup")
#
#rules_jvm_external_setup()
#
#load("@rules_proto_grpc//:repositories.bzl", "rules_proto_grpc_repos", "rules_proto_grpc_toolchains")
#
#rules_proto_grpc_toolchains()
#
#rules_proto_grpc_repos()
#
#load("@rules_proto//proto:repositories.bzl", "rules_proto_dependencies", "rules_proto_toolchains")
#
#rules_proto_dependencies()
#
#rules_proto_toolchains()
#
#load("@rules_proto_grpc//java:repositories.bzl", rules_proto_grpc_java_repos = "java_repos")
#
#rules_proto_grpc_java_repos()
#
#load("@io_grpc_grpc_java//:repositories.bzl", "IO_GRPC_GRPC_JAVA_ARTIFACTS", "IO_GRPC_GRPC_JAVA_OVERRIDE_TARGETS", "grpc_java_repositories")
#
## Protobuf now requires C++14 or higher, which requires Bazel configuration
## outside the WORKSPACE. See .bazelrc in this directory.
#load("@com_google_protobuf//:protobuf_deps.bzl", "PROTOBUF_MAVEN_ARTIFACTS", "protobuf_deps")
#
#protobuf_deps()
#
#load("//deps:maven.bzl", "setup_maven")
#
#setup_maven(
#    IO_GRPC_GRPC_JAVA_ARTIFACTS + PROTOBUF_MAVEN_ARTIFACTS,
#    IO_GRPC_GRPC_JAVA_OVERRIDE_TARGETS,
#)
#
#load("@maven//:compat.bzl", "compat_repositories")
#
#compat_repositories()
#
#grpc_java_repositories()
