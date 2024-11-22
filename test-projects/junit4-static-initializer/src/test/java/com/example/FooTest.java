package com.example;

import io.skippy.junit4.Skippy;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import static junit.framework.TestCase.assertTrue;

public class FooTest {

    @Rule
    public TestRule skippyRule = Skippy.predictWithSkippy();

    static String field;

    static {
        field = ClassUsedInFooTestsInitializer.doSomething();
    }

    @Test
    public void testFoo() {
        assertTrue(Foo.doSomething());
    }
}
