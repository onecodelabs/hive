load("//deps:fetch.bzl", "fetch_all_repositories")

fetch_all_repositories()

# JAVA
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

# JAVASCRIPT
load("@aspect_rules_js//js:repositories.bzl", "rules_js_dependencies")

rules_js_dependencies()

load("@rules_nodejs//nodejs:repositories.bzl", "DEFAULT_NODE_VERSION", "nodejs_register_toolchains")

nodejs_register_toolchains(
    name = "nodejs",
    node_version = DEFAULT_NODE_VERSION,
)

#load("@aspect_rules_js//npm:npm_import.bzl", "npm_translate_lock")
#
#npm_translate_lock(
#    name = "npm",
#    pnpm_lock = "//:pnpm-lock.yaml",
#    #    verify_node_modules_ignored = "//:.bazelignore",
#)
#
#load("@npm//:repositories.bzl", "npm_repositories")
#
#npm_repositories()
