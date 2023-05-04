load("@aspect_rules_js//js:defs.bzl", "js_binary", "js_library")

def js2json(name, input, deps = []):
    js_binary(
        name = name + "_js_binary",
        data = [input] + deps,
        entry_point = "//js:copy_js2json",
        env = {
            "BAZEL_BINDIR": ".",
            "JS2JSON_INPUT": native.package_name() + "/" + input,
        },
    )
    target_name = "//" + native.package_name() + ":" + name + "_js_binary"

    native.genrule(
        name = name,
        outs = [name + ".json"],
        tools = [target_name],
        cmd = "./$(location %s) > \"$@\"" % (target_name),
    )
