java_library(
    name = "helloworld_library",
    srcs = glob(["*.java"]),
    visibility = ["//javatests/com/onecodelabs/helloworld:__pkg__"],
    deps = [
        "//proto/example:user_proto_java",
    ],
)

java_binary(
    name = "helloworld",
    main_class = "com.onecodelabs.helloworld.HelloWorld",
    runtime_deps = [":helloworld_library"],
)
