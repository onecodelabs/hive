package com.onecodelabs.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReflectionUtils {

    public static final Pattern CLASS_PATTERN = Pattern.compile("(\\w+(?:\\.\\w+)*)\\.(\\w+)(?:<(.+)>)?");

    public static Class<?> findClassByName(String clazzName) throws ClassNotFoundException {
        String originalName = clazzName;
        String right = "";
        Matcher m;
        while ((m = CLASS_PATTERN.matcher(clazzName)).matches()) {
            try {
                Class clazz = Class.forName(String.format("%s.%s%s",m.group(1),m.group(2),right));
                return clazz;
            } catch (ClassNotFoundException e) {
                clazzName = m.group(1);
                right = "$" + m.group(2) + right;
            }
        }
        throw new ClassNotFoundException("Did not find class: " + originalName);
    }
}
