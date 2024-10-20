package com.example;

import io.skippy.junit5.PredictWithSkippy;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@PredictWithSkippy
public class NestedTestsTest {

    @Test
    void testSomething() {
        assertEquals("helloA", ClassA.append("hello"));
    }

    @Nested
    class Level2FooTest {

        @Test
        void testSomething() {
            assertEquals("helloB", ClassB.append("hello"));
        }

        @Nested
        class Level3Test {

            @Test
            void testSomething() {
                assertEquals("helloC", ClassC.append("hello"));
            }

        }

    }

    @Nested
    class Level2BarTest {

        @Test
        void testSomething() {
            assertEquals("helloD", ClassD.append("hello"));
        }

    }

}
