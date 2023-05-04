load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

def fetch_all_repositories():
    fetch_io_grpc_grpc_java()
    fetch_rules_jvm_external()
    fetch_aspect_rules_js()

def fetch_rules_jvm_external():
    http_archive(
        name = "rules_jvm_external",
        sha256 = "b17d7388feb9bfa7f2fa09031b32707df529f26c91ab9e5d909eb1676badd9a6",
        strip_prefix = "rules_jvm_external-%s" % "4.5",
        url = "https://github.com/bazelbuild/rules_jvm_external/archive/%s.zip" % "4.5",
    )

def fetch_io_grpc_grpc_java():
    http_archive(
        name = "io_grpc_grpc_java",
        sha256 = "75e5994ca18ae3ffaf67a2f08d0274215916b0ff56d62e9e5b447095e622714b",
        strip_prefix = "grpc-java-%s" % "1.51.1",
        url = "https://github.com/grpc/grpc-java/archive/v%s.zip" % "1.51.1",
    )

def fetch_aspect_rules_js():
    http_archive(
        name = "aspect_rules_js",
        sha256 = "3e237129b3554373a80c681c4b47348f91c294ff32d4bc8f8297f40511a4eb6c",
        strip_prefix = "rules_js-1.25.4",
        url = "https://github.com/aspect-build/rules_js/releases/download/v1.25.4/rules_js-v1.25.4.tar.gz",
    )
