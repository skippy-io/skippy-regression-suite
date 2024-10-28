package com.example;

import io.skippy.junit5.PredictWithSkippy;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@PredictWithSkippy
public class RootTest {

    @Nested
    class NestedTest {
        @Test
        void testSomething() {
            assertEquals("something$", ProductionClass.returnSomething());
        }

    }


}
