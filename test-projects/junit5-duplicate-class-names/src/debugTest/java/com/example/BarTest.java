package com.example;

import io.skippy.junit5.PredictWithSkippy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@PredictWithSkippy
public class BarTest {

    @Test
    void testBar() {
        assertEquals("debugTest", new Bar().doSomething());
    }

}