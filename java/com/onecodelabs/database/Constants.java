package com.onecodelabs.database;

import java.util.regex.Pattern;

public class Constants {
    // E.g.: proto.example.User
    // group1: proto.example
    // group2: User
    public static final Pattern PATTERN_IMPORT = Pattern.compile("^(\\w+(?:\\.\\w+)+)\\.(\\w+)$");

    // E.g.: User proto.example.User
    // group1: User
    // group2: proto.example
    // group3: User
    public static final Pattern PATTERN_COLUMN = Pattern.compile("^(\\w+) (\\w+(?:\\.\\w+)+)\\.(\\w+)$");

    // E.g.: MyMessage.foo.bar.asd.xyz
    // group1: MyMessage
    // group2: foo.bar.asd.xyz
    public static final Pattern PATTERN_MESSAGE_ACCESS = Pattern.compile("^(\\w+)\\.(\\w+(?:\\.\\w+)*)$");

    // E.g.: User.address.foo User.address.foo
    // note: this is a sequence of space-separated PATTERN_MESSAGE_ACCESS
    public static final Pattern PATTERN_PRIMARY_KEYS =
            Pattern.compile("^\\w+\\.(?:\\w+(?:\\.\\w+)*)(?: \\w+\\.(?:\\w+(?:\\.\\w+)*))*$");
}
