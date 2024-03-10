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

public class JUnit4SmokeTest {

    @Test
    @Tag(SkippyTestTag.GRADLE)
    public void testBuild() throws Exception {
        var projectDir = new File(getClass().getResource("/test-projects/junit4-smoketest").toURI());
        GradleRunner.create()
                .withProjectDir(projectDir)
                .withArguments(refresh("clean", "skippyClean", "check"))
                .forwardOutput()
                .build();

        var predictionsLog = projectDir.toPath().resolve(".skippy").resolve("predictions.log");
        assertThat(readAllLines(predictionsLog, StandardCharsets.UTF_8).toArray()).containsExactlyInAnyOrder(
            "com.example.LeftPadderTest,EXECUTE,NO_DATA_FOUND_FOR_TEST",
            "com.example.RightPadderTest,EXECUTE,NO_DATA_FOUND_FOR_TEST"
        );

        var tia = Files.readString(projectDir.toPath().resolve(".skippy/test-impact-analysis.json"), StandardCharsets.UTF_8);

        JSONAssert.assertEquals("""
            {
                "classes": {
                    "0": {
                        "name": "com.example.LeftPadder",
                        "path": "com/example/LeftPadder.class",
                        "outputFolder": "build/classes/java/main",
                        "hash": "8E994DD8"
                    },
                    "1": {
                        "name": "com.example.LeftPadderTest",
                        "path": "com/example/LeftPadderTest.class",
                        "outputFolder": "build/classes/java/test",
                        "hash": "5BC2F2A3"
                    },
                    "2": {
                        "name": "com.example.RightPadder",
                        "path": "com/example/RightPadder.class",
                        "outputFolder": "build/classes/java/main",
                        "hash": "F7F27006"
                    },
                    "3": {
                        "name": "com.example.RightPadderTest",
                        "path": "com/example/RightPadderTest.class",
                        "outputFolder": "build/classes/java/test",
                        "hash": "7966371F"
                    },
                    "4": {
                        "name": "com.example.StringUtils",
                        "path": "com/example/StringUtils.class",
                        "outputFolder": "build/classes/java/main",
                        "hash": "ECE5D94D"
                    },
                    "5": {
                        "name": "com.example.StringUtilsTest",
                        "path": "com/example/StringUtilsTest.class",
                        "outputFolder": "build/classes/java/test",
                        "hash": "08B76AA9"
                    },
                    "6": {
                        "name": "com.example.TestConstants",
                        "path": "com/example/TestConstants.class",
                        "outputFolder": "build/classes/java/test",
                        "hash": "119F463C"
                    }
                },
                "tests": [
                    {
                        "class": "1",
                        "result": "PASSED",
                        "coveredClasses": ["0","1","4"]
                    },
                    {
                        "class": "3",
                        "result": "PASSED",
                        "coveredClasses": ["2","3","4"]
                    }
                ]
            }
        """, tia, JSONCompareMode.LENIENT);
    }

}