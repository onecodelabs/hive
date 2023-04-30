proto_library(
    name = "user_proto",
    srcs = ["user.proto"],
    visibility = ["//visibility:public"],
)

java_proto_library(
    name = "user_proto_java",
    visibility = ["//java/com/onecodelabs/helloworld:__pkg__"],
    deps = [":user_proto"],
)
