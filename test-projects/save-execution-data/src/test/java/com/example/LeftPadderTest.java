package com.example;


import io.skippy.junit5.PredictWithSkippy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@PredictWithSkippy
public class LeftPadderTest {

    @Test
    public void testPadLeft() {
        assertEquals(" hello", LeftPadder.padLeft("hello", 6));
    }

}
