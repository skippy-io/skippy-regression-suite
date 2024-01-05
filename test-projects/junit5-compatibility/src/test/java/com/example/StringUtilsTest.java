package com.example;

import io.skippy.junit5.Skippified;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Skippified
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