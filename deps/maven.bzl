load("@rules_jvm_external//:defs.bzl", "maven_install")

def setup_maven(extra_artifacts = [], override_targets = []):
    maven_install(
        artifacts = [
            "com.google.auto.service:auto-service:1.0.1",
            "com.google.auto.service:auto-service-annotations:1.0.1",
            "com.google.protobuf:protobuf-java:3.22.3",
            "com.google.protobuf:protobuf-java-util:3.22.3",
            "com.google.guava:guava:31.1-jre",
            "junit:junit:4.13.2",
            "com.google.inject:guice:5.1.0",
        ] + extra_artifacts,
        generate_compat_repositories = True,
        override_targets = override_targets,
        repositories = [
            "https://repo.maven.apache.org/maven2/",
        ],
        strict_visibility = True,
    )
