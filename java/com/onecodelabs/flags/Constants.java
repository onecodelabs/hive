package com.onecodelabs.flags;

import java.util.regex.Pattern;

public class Constants {
    public static final Pattern BOOLEAN_FLAG_PATTERN = Pattern.compile("--(\\w+)");
    public static final Pattern VALUE_FLAG_PATTERN = Pattern.compile("--(\\w+)=(.+)");
    public static final Pattern CLASS_PATTERN = Pattern.compile("(\\w+(?:\\.\\w+)*)\\.(\\w+)(?:<(.+)>)?");
}
