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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static io.skippy.test.gradle.Tasks.refresh;

public class GradleCompatibilityTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "7.3",
            "7.4",
            "7.5",
            "7.6",
            "8.0",
            "8.1",
            "8.2",
            "8.3",
            "8.4",
            "8.5",
            "8.6"
    })
    @Tag(SkippyTestTag.GRADLE)
    public void testBuild(String gradleVersion) throws Exception {
        var projectDir = new File(getClass().getResource("/test-projects/gradle-compatibility").toURI());
        GradleRunner.create()
                .withProjectDir(projectDir)
                .withGradleVersion(gradleVersion)
                .withArguments(refresh("clean", "skippyClean", "check", "--stacktrace"))
                .forwardOutput()
                .build();

        var tia = Files.readString(projectDir.toPath().resolve(".skippy/test-impact-analysis.json"), StandardCharsets.UTF_8);

        JSONAssert.assertEquals("""
            {
                "classes": {
                    "0": {
                        "name": "com.example.StringUtils"
                    },
                    "1": {
                        "name": "com.example.StringUtilsTest"
                    }
                },
                "tests": [
                    {
                        "class": 1,
                        "result": "PASSED",
                        "coveredClasses": [0,1]
                    }
                ]
            }
        """, tia, JSONCompareMode.LENIENT);
    }

}