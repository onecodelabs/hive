load("@rules_java//java:defs.bzl", "java_binary", "java_library")
#load("@io_bazel_rules_docker//java:image.bzl", "java_image")

java_library(
    name = "locks_library",
    srcs = glob(["*.java"]),
    deps = [
        "//java/com/onecodelabs/common/flags",
        "//proto/locks:lock_proto_java",
        "//proto/locks:lock_service_proto_java",
    ],
)

#java_image(
#    name = "locks_java_image",
#    main_class = "com.onecodelabs.locks.LockServer",
#)

java_binary(
    name = "locks",
    main_class = "com.onecodelabs.locks.LockServer",
    runtime_deps = [":locks_library"],
)
