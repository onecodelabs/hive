load("@rules_java//java:defs.bzl", "java_binary", "java_library")

java_library(
    name = "locks_library",
    srcs = glob(["*.java"]),
    deps = [
        "//proto/locks:lock_proto_java",
        "//proto/locks:lock_service_proto_java",
    ],
)

java_binary(
    name = "locks",
    main_class = "com.onecodelabs.locks.LockServer",
    runtime_deps = [":locks_library"],
)
