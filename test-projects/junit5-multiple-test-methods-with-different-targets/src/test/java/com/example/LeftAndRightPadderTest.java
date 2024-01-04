package com.example;

import io.skippy.junit5.Skippified;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Skippified
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