java_test(
    name = "FlagsTest",
    srcs = ["FlagsTest.java"],
    jvm_flags = [
        "-Dstring_flag=foo",
        "-Dstring_list_flag=foo,bar",
        "-Dinteger_flag=4",
        "-Dinteger_list_flag=1,2",
        "-Dboolean_flag=true",
        "-Denum_flag=RED",
        "-Denum_list_flag=GREEN,BLUE",
    ],
    deps = ["//java/com/onecodelabs/flags"],
)
