package com.example;

import io.skippy.junit4.Skippy;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TestRule;

import static org.junit.Assert.assertEquals;

public class RightPadderTest {

    @ClassRule
    public static TestRule skippyRule = Skippy.predictWithSkippy();

    @Test
    public void testPadLeft() {
        var input = TestConstants.HELLO;
        assertEquals("hello ", RightPadder.padRight(input, 6));
    }

}
