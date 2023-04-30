load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

def fetch_all_repositories():
    fetch_io_grpc_grpc_java()
    fetch_rules_jvm_external()

#    fetch_rules_proto()
#    fetch_rules_proto_grpc()

def fetch_rules_jvm_external():
    http_archive(
        name = "rules_jvm_external",
        sha256 = "b17d7388feb9bfa7f2fa09031b32707df529f26c91ab9e5d909eb1676badd9a6",
        strip_prefix = "rules_jvm_external-%s" % "4.5",
        url = "https://github.com/bazelbuild/rules_jvm_external/archive/%s.zip" % "4.5",
    )

def fetch_rules_proto():
    http_archive(
        name = "rules_proto",
        sha256 = "dc3fb206a2cb3441b485eb1e423165b231235a1ea9b031b4433cf7bc1fa460dd",
        strip_prefix = "rules_proto-5.3.0-21.7",
        urls = [
            "https://github.com/bazelbuild/rules_proto/archive/refs/tags/5.3.0-21.7.tar.gz",
        ],
    )

def fetch_rules_proto_grpc():
    http_archive(
        name = "rules_proto_grpc",
        sha256 = "fb7fc7a3c19a92b2f15ed7c4ffb2983e956625c1436f57a3430b897ba9864059",
        strip_prefix = "rules_proto_grpc-4.3.0",
        urls = ["https://github.com/rules-proto-grpc/rules_proto_grpc/archive/4.3.0.tar.gz"],
    )

def fetch_io_grpc_grpc_java():
    http_archive(
        name = "io_grpc_grpc_java",
        sha256 = "75e5994ca18ae3ffaf67a2f08d0274215916b0ff56d62e9e5b447095e622714b",
        strip_prefix = "grpc-java-%s" % "1.51.1",
        url = "https://github.com/grpc/grpc-java/archive/v%s.zip" % "1.51.1",
    )
