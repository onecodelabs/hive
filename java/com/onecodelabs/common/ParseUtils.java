package com.onecodelabs.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseUtils {

    public static String group(String s, Pattern pattern, int n) {
        Matcher m = pattern.matcher(s);
        if (!m.matches()) {
            throw new IllegalArgumentException(String.format("String %s doesn't conform to pattern %s", s, pattern));
        }

        return m.group(n);
    }
}
