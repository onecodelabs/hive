load("@rules_proto//proto:defs.bzl", "proto_library")
load("@rules_java//java:defs.bzl", "java_binary", "java_proto_library")
load("//js:defs.bzl", "js2json")

def absolute_target(target):
    if target.startswith("//"):
        return target
    if target.startswith(":"):
        return "//" + native.package_name() + target
    return target

# genrule that converts a .json file into a .textproto file.
def json2proto(name, json_file, proto_file = None, proto_message = None, proto_deps = [], proto_lib = None):
    if not proto_lib:
        if not proto_file:
            fail("Either proto_file or proto_lib need to be specified")
        proto_library(
            name = name + "_proto_library",
            srcs = [proto_file],
            deps = proto_deps,
        )
        proto_lib = "//" + native.package_name() + ":" + name + "_proto_library"

    proto_library_target = proto_lib

    java_proto_library_target_name = name + "java_proto_library"
    java_proto_library(
        name = java_proto_library_target_name,
        deps = [proto_library_target],
    )
    java_proto_library_target = "//" + native.package_name() + ":" + java_proto_library_target_name

    java_binary_target_name = name + "_java_binary"
    java_binary(
        name = java_binary_target_name,
        main_class = "com.onecodelabs.json2proto.Json2Proto",
        runtime_deps = [
            "//java/com/onecodelabs/json2proto:Json2Proto",
            java_proto_library_target,
        ],
        data = [
            json_file,
            proto_library_target,
        ],
    )
    java_binary_target = "//" + native.package_name() + ":" + java_binary_target_name

    def _handle_file_path(file):
        if file.startswith("//"):
            return file
        return "//" + native.package_name() + ":" + file

    native.genrule(
        name = name,
        outs = [name + ".textproto"],
        srcs = [
            json_file,
            proto_library_target,
        ],
        tools = [java_binary_target],
        cmd = "./$(location %s) --json_file=$(location %s) --proto_file=$(location %s) --proto_message=%s > \"$@\"" % (
            java_binary_target,
            _handle_file_path(json_file),
            _handle_file_path(proto_library_target),
            proto_message,
        ),
    )

# genrule that converts a .js file into a .textproto file.
def js2proto(name, input, proto_message, proto_file = None, proto_lib = None, js_deps = [], proto_deps = []):
    js2json(
        name = name + "_js2proto",
        input = input,
        deps = js_deps,
    )
    js2json_target = "//" + native.package_name() + ":" + name + "_js2proto"

    json2proto(
        name = name,
        json_file = js2json_target,
        proto_file = proto_file,
        proto_lib = proto_lib,
        proto_deps = proto_deps,
        proto_message = proto_message,
    )
