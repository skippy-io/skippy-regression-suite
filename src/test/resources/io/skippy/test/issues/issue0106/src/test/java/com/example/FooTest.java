package com.example;

import io.skippy.junit5.Skippified;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Skippified
@ExtendWith(ExceptionThrowingExtension.class)
public class FooTest {

    @Test
    void testNothing() {
        assertTrue(true);
    }

}