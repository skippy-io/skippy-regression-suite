/*
 * Copyright 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.skippy.test.gradle;

import io.skippy.test.SkippyTestTag;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static io.skippy.test.gradle.Tasks.refresh;
import static java.nio.file.Files.readAllLines;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class JUnit5NestedTestsTest {

    @Test
    @Tag(SkippyTestTag.GRADLE)
    public void testBuild() throws Exception {
        var projectDir = new File(getClass().getResource("/test-projects/junit5-nested-tests").toURI());
        GradleRunner.create()
                .withProjectDir(projectDir)
                .withArguments(refresh("clean", "skippyClean", "check"))
                .build();

        var predictionsLog = projectDir.toPath().resolve(".skippy").resolve("predictions.log");
        assertThat(readAllLines(predictionsLog, StandardCharsets.UTF_8).toArray()).containsExactlyInAnyOrder(
                "build/classes/java/test,com.example.NestedTestsTest,EXECUTE,TEST_IMPACT_ANALYSIS_NOT_FOUND",
                "build/classes/java/test,com.example.NestedTestsTest$Level2BarTest,EXECUTE,TEST_IMPACT_ANALYSIS_NOT_FOUND",
                "build/classes/java/test,com.example.NestedTestsTest$Level2FooTest,EXECUTE,TEST_IMPACT_ANALYSIS_NOT_FOUND",
                "build/classes/java/test,com.example.NestedTestsTest$Level2FooTest$Level3Test,EXECUTE,TEST_IMPACT_ANALYSIS_NOT_FOUND"
        );

        var tia = Files.readString(projectDir.toPath().resolve(".skippy/test-impact-analysis.json"), StandardCharsets.UTF_8);

        JSONAssert.assertEquals("""
            {
                "id": "1C32725820E02BF7BC21749F0991E741",
                "classes": {
                    "0": {
                        "name": "com.example.ClassA",
                        "path": "com/example/ClassA.class",
                        "outputFolder": "build/classes/java/main",
                        "hash": "3041DC84"
                    },
                    "1": {
                        "name": "com.example.ClassB",
                        "path": "com/example/ClassB.class",
                        "outputFolder": "build/classes/java/main",
                        "hash": "37FA6DE7"
                    },
                    "2": {
                        "name": "com.example.ClassC",
                        "path": "com/example/ClassC.class",
                        "outputFolder": "build/classes/java/main",
                        "hash": "B17DF734"
                    },
                    "3": {
                        "name": "com.example.ClassD",
                        "path": "com/example/ClassD.class",
                        "outputFolder": "build/classes/java/main",
                        "hash": "41C10CEE"
                    },
                    "4": {
                        "name": "com.example.NestedTestsTest",
                        "path": "com/example/NestedTestsTest.class",
                        "outputFolder": "build/classes/java/test",
                        "hash": "C3A3737F"
                    },
                    "5": {
                        "name": "com.example.NestedTestsTest$Level2BarTest",
                        "path": "com/example/NestedTestsTest$Level2BarTest.class",
                        "outputFolder": "build/classes/java/test",
                        "hash": "5780C183"
                    },
                    "6": {
                        "name": "com.example.NestedTestsTest$Level2FooTest",
                        "path": "com/example/NestedTestsTest$Level2FooTest.class",
                        "outputFolder": "build/classes/java/test",
                        "hash": "3E35762B"
                    },
                    "7": {
                        "name": "com.example.NestedTestsTest$Level2FooTest$Level3Test",
                        "path": "com/example/NestedTestsTest$Level2FooTest$Level3Test.class",
                        "outputFolder": "build/classes/java/test",
                        "hash": "FA83D453"
                    }
                },
                "tests": [
                    {
                        "class": 4,
                        "tags": ["PASSED"],
                        "coveredClasses": [0,1,2,3,4,5,6,7]
                    },
                    {
                        "class": 5,
                        "tags": ["PASSED"],
                        "coveredClasses": [3,4,5]
                    },
                    {
                        "class": 6,
                        "tags": ["PASSED"],
                        "coveredClasses": [1,2,4,6,7]
                    },
                    {
                        "class": 7,
                        "tags": ["PASSED"],
                        "coveredClasses": [2,4,6,7]
                    }
                ]
            }
        """, tia, JSONCompareMode.LENIENT);
    }

}