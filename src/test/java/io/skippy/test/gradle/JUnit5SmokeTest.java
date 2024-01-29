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

import io.skippy.common.model.TestImpactAnalysis;
import io.skippy.test.SkippyTestTag;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;

import static java.nio.file.Files.readAllLines;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class JUnit5SmokeTest {

    @Test
    @Tag(SkippyTestTag.GRADLE)
    public void testBuild() throws Exception {
        var projectDir = new File(getClass().getResource("/test-projects/junit5-smoketest").toURI());
        GradleRunner.create()
                .withProjectDir(projectDir)
                .withArguments("skippyAnalyze", "--refresh-dependencies")
                .build();

        var predictionsLog = projectDir.toPath().resolve(".skippy").resolve("predictions.log");
        assertThat(readAllLines(predictionsLog, StandardCharsets.UTF_8).toArray()).containsExactlyInAnyOrder(
                "com.example.LeftPadderTest:EXECUTE:UNKNOWN_TEST",
                "com.example.RightPadderTest:EXECUTE:UNKNOWN_TEST"
        );

        var tia = TestImpactAnalysis.readFromFile(projectDir.toPath().resolve(".skippy").resolve("test-impact-analysis.json"));
        assertThat(tia.toJson()).isEqualToIgnoringWhitespace("""
            [
                {
                    "testClass": {
                        "class": "com.example.LeftPadderTest",
                        "path": "com/example/LeftPadderTest.class",
                        "outputFolder": "build/classes/java/test",
                        "hash": "sGLJTZJw4beE9m2Kg6chUg=="
                    },
                    "result": "SUCCESS",
                    "coveredClasses": [
                        {
                            "class": "com.example.LeftPadder",
                            "path": "com/example/LeftPadder.class",
                            "outputFolder": "build/classes/java/main",
                            "hash": "9U3+WYit7uiiNqA9jplN2A=="
                        },
                        {
                            "class": "com.example.LeftPadderTest",
                            "path": "com/example/LeftPadderTest.class",
                            "outputFolder": "build/classes/java/test",
                            "hash": "sGLJTZJw4beE9m2Kg6chUg=="
                        },
                        {
                            "class": "com.example.StringUtils",
                            "path": "com/example/StringUtils.class",
                            "outputFolder": "build/classes/java/main",
                            "hash": "4VP9fWGFUJHKIBG47OXZTQ=="
                        }
                    ]
                },
                {
                    "testClass": {
                        "class": "com.example.RightPadderTest",
                        "path": "com/example/RightPadderTest.class",
                        "outputFolder": "build/classes/java/test",
                        "hash": "wAwQMlDS3xxmX/Yl5fsSdA=="
                    },
                    "result": "SUCCESS",
                    "coveredClasses": [
                        {
                            "class": "com.example.RightPadder",
                            "path": "com/example/RightPadder.class",
                            "outputFolder": "build/classes/java/main",
                            "hash": "ZT0GoiWG8Az5TevH9/JwBg=="
                        },
                        {
                            "class": "com.example.RightPadderTest",
                            "path": "com/example/RightPadderTest.class",
                            "outputFolder": "build/classes/java/test",
                            "hash": "wAwQMlDS3xxmX/Yl5fsSdA=="
                        },
                        {
                            "class": "com.example.StringUtils",
                            "path": "com/example/StringUtils.class",
                            "outputFolder": "build/classes/java/main",
                            "hash": "4VP9fWGFUJHKIBG47OXZTQ=="
                        }
                    ]
                }
            ]
        """);
    }

}