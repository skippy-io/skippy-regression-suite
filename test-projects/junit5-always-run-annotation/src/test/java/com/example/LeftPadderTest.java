package com.example;

import io.skippy.core.AlwaysRun;
import io.skippy.junit5.PredictWithSkippy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@AlwaysRun
@PredictWithSkippy
public class LeftPadderTest {

    @Test
    void testPadLeft() {
        var input = TestConstants.HELLO;
        assertEquals(" hello", LeftPadder.padLeft(input, 6));
    }

}
