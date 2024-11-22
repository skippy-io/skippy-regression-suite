package com.example;

import io.skippy.junit4.Skippy;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import static org.junit.Assert.assertEquals;

public class StringUtilsTest {

    @Rule
    public TestRule skippyRule = Skippy.predictWithSkippy();

    @Test
    public void testPadLeft() {
        assertEquals(" hello", StringUtils.padLeft("hello", 6));
    }

    @Test
    public void testPadRight() {
        assertEquals("hello ", StringUtils.padRight("hello", 6));
    }

}