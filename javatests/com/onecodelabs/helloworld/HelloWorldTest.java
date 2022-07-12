package com.onecodelabs.helloworld;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;

public class HelloWorldTest {

    @Test
    public void somethingTest() {
        assertEquals(2, 1 + 1);
    }

    @Test
    public void greet_returnsProperGreeting() {
        assertEquals("Hello world!", HelloWorld.greet());
    }
}
