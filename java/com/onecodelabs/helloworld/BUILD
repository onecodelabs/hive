load("@rules_java//java:defs.bzl", "java_binary", "java_library")

java_library(
    name = "helloworld_library",
    srcs = glob(["*.java"]),
    visibility = ["//javatests/com/onecodelabs/helloworld:__pkg__"],
    deps = ["//models:user_proto_java"],
)

java_binary(
    name = "helloworld",
    main_class = "com.onecodelabs.helloworld.HelloWorld",
    runtime_deps = [":helloworld_library"],
)
