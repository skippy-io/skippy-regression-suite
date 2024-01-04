package com.example;

import io.skippy.junit4.Skippy;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TestRule;

import static junit.framework.TestCase.assertTrue;

public class FooTest {

    @ClassRule
    public static TestRule skippyRule = Skippy.skippify();

    static String field;

    static {
        field = ClassUsedInFooTestsInitializer.doSomething();
    }

    @Test
    public void testFoo() {
        assertTrue(Foo.doSomething());
    }
}
