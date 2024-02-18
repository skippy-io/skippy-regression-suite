package com.example;

import io.skippy.junit5.Skippified;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Skippified
public class BarTest {

    @Test
    void testGetBar() {
        assertEquals("bar", Bar.getBar());
    }

}
