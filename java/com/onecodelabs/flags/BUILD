java_plugin(
    name = "processor",
    srcs = [
        "Constants.java",
        "FlagSpec.java",
        "FlagsProcessor.java",
    ],
    processor_class = "com.onecodelabs.flags.FlagsProcessor",
    deps = [
        ":flag_proto_java",
        "@maven//:com_google_auto_service_auto_service_annotations",
    ],
)

proto_library(
    name = "flag_proto",
    srcs = ["flag.proto"],
)

java_proto_library(
    name = "flag_proto_java",
    deps = [":flag_proto"],
)

java_library(
    name = "flags",
    srcs = [
        "Constants.java",
        "Flag.java",
        "FlagSpec.java",
        "Flags.java",
    ],
    exported_plugins = [":processor"],
    visibility = ["//visibility:public"],
    exports = [":flag_proto_java"],
    deps = [":flag_proto_java"],
)

# Example used for testing
java_library(
    name = "flags_example_library",
    srcs = ["FlagsExample.java"],
    deps = [
        ":flag_proto_java",
        ":flags",
    ],
)

java_binary(
    name = "flags_example",
    main_class = "com.onecodelabs.flags.FlagsExample",
    runtime_deps = [":flags_example_library"],
)
