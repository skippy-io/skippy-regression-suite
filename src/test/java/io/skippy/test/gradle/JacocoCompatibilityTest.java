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
import java.util.Map;

import static io.skippy.test.gradle.Tasks.refresh;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class JacocoCompatibilityTest {

    @ParameterizedTest
    @ValueSource(strings = {
//            "0.8.7",
//            "0.8.8",
//            "0.8.10",
            "0.8.11"
    })
    @Tag(SkippyTestTag.GRADLE)
    public void testBuild(String jacocoVersion) throws Exception {
        var projectDir = new File(getClass().getResource("/test-projects/jacoco-compatibility").toURI());
        GradleRunner.create()
                .withProjectDir(projectDir)
                .withEnvironment(Map.of("jacocoVersion", jacocoVersion))
                .withArguments(refresh("clean", "skippyClean", "check"))
                .forwardOutput()
                .build();

        var tia = TestImpactAnalysis.readFromFile(projectDir.toPath().resolve(".skippy").resolve("test-impact-analysis.json"));
        assertThat(tia.toJson(JsonProperty.CLASS_NAME)).isEqualToIgnoringWhitespace("""
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
                        "class": "1",
                        "coveredClasses": ["0","1"]
                    }
                ]
            }
        """);
    }

}