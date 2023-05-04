package com.onecodelabs.common;

public class AssertUtils {

    public static void assumeTrue(String errorMessage, boolean shouldBeTrue) {
        if (!shouldBeTrue) {
            throw new IllegalStateException(errorMessage);
        }
    }
}
