package com.example;

import io.skippy.junit5.PredictWithSkippy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


@PredictWithSkippy
public class FooTest {
    @Test
    void testFoo() {
        assertEquals("main", new Foo().doSomething());
    }
}