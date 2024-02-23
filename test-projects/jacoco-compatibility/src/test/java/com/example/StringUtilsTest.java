package com.example;

import io.skippy.junit5.PredictWithSkippy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@PredictWithSkippy
public class StringUtilsTest {

    @Test
    void testPadLeft() {
        assertEquals(" hello", StringUtils.padLeft("hello", 6));
    }

    @Test
    void testPadRight() {
        assertEquals("hello ", StringUtils.padRight("hello", 6));
    }

}