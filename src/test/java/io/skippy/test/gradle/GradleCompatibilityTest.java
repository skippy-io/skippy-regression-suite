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

import io.skippy.common.model.JsonProperty;
import io.skippy.common.model.TestImpactAnalysis;
import io.skippy.test.SkippyTestTag;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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
            "8.5"
    })
    @Tag(SkippyTestTag.GRADLE)
    public void testBuild(String gradleVersion) throws Exception {
        var projectDir = new File(getClass().getResource("/test-projects/gradle-compatibility").toURI());
        GradleRunner.create()
                .withProjectDir(projectDir)
                .withGradleVersion(gradleVersion)
                .withArguments("skippyClean", "check", "--refresh-dependencies")
                .forwardOutput()
                .build();

        var tia = TestImpactAnalysis.readFromFile(projectDir.toPath().resolve(".skippy").resolve("test-impact-analysis.json"));
        assertThat(tia.toJson(JsonProperty.CLASS_NAME)).isEqualToIgnoringWhitespace("""
            [
                {
                    "testClass": {
                        "class": "com.example.StringUtilsTest"
                    },
                    "result": "SUCCESS",
                    "coveredClasses": [
                        {
                            "class": "com.example.StringUtils"
                        },
                        {
                            "class": "com.example.StringUtilsTest"
                        }
                    ]
                }
            ]
        """);
    }

}