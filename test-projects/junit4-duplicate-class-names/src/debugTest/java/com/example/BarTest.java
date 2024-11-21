package com.example;


import io.skippy.junit4.Skippy;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TestRule;

import static org.junit.Assert.assertEquals;

public class BarTest {

    @ClassRule
    public static TestRule skippyRule = Skippy.predictWithSkippy();

    @Test
    public void testBar() {
        assertEquals("debugTest", new Bar().doSomething());
    }

}