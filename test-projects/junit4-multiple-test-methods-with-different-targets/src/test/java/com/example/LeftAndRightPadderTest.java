package com.example;

import io.skippy.junit4.Skippy;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TestRule;

import static org.junit.Assert.assertEquals;

public class LeftAndRightPadderTest {

    @ClassRule
    public static TestRule skippyRule = Skippy.skippify();

    @Test
    public void testPadLeft() {
        assertEquals(" hello", LeftPadder.padLeft("hello", 6));
    }

    @Test
    public void testPadRight() {
        assertEquals("hello ", RightPadder.padRight("hello", 6));
    }

}