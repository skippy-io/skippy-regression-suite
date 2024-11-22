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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;

import static io.skippy.core.SkippyRegressionTestApi.deleteDirectory;
import static io.skippy.test.gradle.Tasks.refresh;
import static java.util.Arrays.asList;

public class JUnit5TestFailureTest {

    @Test
    @Tag(SkippyTestTag.GRADLE)
    public void testBuild() throws Exception {
        var projectDir = new File(getClass().getResource("/test-projects/junit5-test-failure").toURI());

        deleteDirectory(projectDir.toPath().resolve(".skippy"));

        var arguments = new ArrayList<>(asList("clean", "skippyClean", "check", "--stacktrace"));

        GradleRunner.create()
                .withProjectDir(projectDir)
                .withArguments(refresh(arguments))
                .buildAndFail();

        var tia = Files.readString(projectDir.toPath().resolve(".skippy/test-impact-analysis.json"), StandardCharsets.UTF_8);

        JSONAssert.assertEquals("""
            {
                "classes": {
                    "0": {
                        "name": "com.example.LeftPadder"
                    },
                    "1": {
                        "name": "com.example.LeftPadderTest"
                    },
                    "2": {
                        "name": "com.example.RightPadder"
                    },
                    "3": {
                        "name": "com.example.RightPadderTest"
                    },
                    "4": {
                        "name": "com.example.StringUtils"
                    },
                    "5": {
                        "name": "com.example.StringUtilsTest"
                    },
                    "6": {
                        "name": "com.example.TestConstants"
                    }
                },
                "tests": [
                    {
                        "class": 1,
                        "tags": ["PASSED"],
                        "coveredClasses": [0,1,4]
                    },
                    {
                        "class": 3,
                        "tags": ["FAILED"],
                        "coveredClasses": [2,3,4]
                    }
                ]
            }
        """, tia, JSONCompareMode.LENIENT);

    }

}