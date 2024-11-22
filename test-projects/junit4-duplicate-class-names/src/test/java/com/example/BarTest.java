package com.example;


import io.skippy.junit4.Skippy;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import static org.junit.Assert.assertEquals;

public class BarTest {

    @Rule
    public TestRule skippyRule = Skippy.predictWithSkippy();

    @Test
    public void testBar() {
        assertEquals("main", new Bar().doSomething());
    }

}