package com.example;

import io.skippy.junit5.PredictWithSkippy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertTrue;

@PredictWithSkippy
@ExtendWith(ExtensionThatShouldNotBeExecuted.class)
public class FooTest {

    @Test
    void testNothing() {
        assertTrue(true);
    }

}