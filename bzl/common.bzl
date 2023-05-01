def absolute_target(target):
    if target.startswith("//"):
        return target
    if target.startswith(":"):
        return "//" + native.package_name() + target
    return target
