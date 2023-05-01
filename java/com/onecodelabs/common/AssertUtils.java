package com.onecodelabs.common;

public class AssertUtils {

    public static void assume(String errorMessage, boolean shouldBeTrue) {
        if (!shouldBeTrue) {
            throw new IllegalStateException(errorMessage);
        }
    }
}
