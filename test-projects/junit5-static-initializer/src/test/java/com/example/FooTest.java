package com.example;

import io.skippy.junit5.Skippified;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Skippified
public class FooTest {

    static String field;

    static {
        field = ClassUsedInFooTestsInitializer.doSomething();
    }

    @Test
    public void testFoo() {
        assertTrue(Foo.doSomething());
    }
}
