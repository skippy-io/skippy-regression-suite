package com.example;

import io.skippy.junit5.PredictWithSkippy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertEquals;

@PredictWithSkippy
// @TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LeftAndRightPadderTest {

    @Test
    public void testPadLeft() {
        assertEquals(" hello", LeftPadder.padLeft("hello", 6));
    }

    @Test
    public void testPadRight() {
        assertEquals("hello ", RightPadder.padRight("hello", 6));
    }

}