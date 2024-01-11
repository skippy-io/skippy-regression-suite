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
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Map;

import static java.nio.file.Files.readAllLines;
import static java.nio.file.Files.readString;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Test Impact Analysis using multiple versions of Gradle
 *
 * @author Florian McKee
 */

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
        BuildResult result = GradleRunner.create()
                .withProjectDir(projectDir)
                .withGradleVersion(gradleVersion)
                .withArguments("skippyAnalyze", "--refresh-dependencies")
                .forwardOutput()
                .build();

        // for troubleshooting purposes
        var output = result.getOutput();

        var predictionsLog = projectDir.toPath().resolve(Path.of("skippy", "predictions.log"));
        assertThat(readAllLines(predictionsLog, StandardCharsets.UTF_8).toArray()).containsExactlyInAnyOrder(
         "com.example.StringUtilsTest:EXECUTE:NO_COVERAGE_DATA_FOR_TEST"
        );

        var classesMd5Txt = projectDir.toPath().resolve(Path.of("skippy", "classes.md5"));
        assertThat(readString(classesMd5Txt, StandardCharsets.UTF_8)).isEqualTo("""
            build/classes/java/main:com/example/StringUtils.class:4VP9fWGFUJHKIBG47OXZTQ==
            build/classes/java/test:com/example/StringUtilsTest.class:U6eTj3290gUo4qeUMST9TQ==""");

        var stringUtilsTest = projectDir.toPath().resolve(Path.of("skippy", "com.example.StringUtilsTest.cov"));
        assertThat(readString(stringUtilsTest , StandardCharsets.UTF_8)).isEqualTo("""
            com.example.StringUtils
            com.example.StringUtilsTest
            """);
    }

}