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

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static java.nio.file.Files.readAllLines;
import static java.nio.file.Files.readString;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Test Impact Analysis using multiple versions of JUnit 4
 *
 * @author Florian McKee
 */

public class JUnit4CompatibilityTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "4.10",
            "4.11",
            "4.13",
            "4.13.1",
            "4.13.2"
    })
    @Tag(SkippyTestTag.GRADLE)
    public void testBuild(String junit5Version) throws Exception {
        var projectDir = new File(getClass().getResource("/test-projects/junit4-compatibility").toURI());
        GradleRunner.create()
                .withProjectDir(projectDir)
                .withEnvironment(Map.of("junit4Version", junit5Version))
                .withArguments("skippyAnalyze", "--refresh-dependencies")
                .forwardOutput()
                .build();

        var predictionsLog = projectDir.toPath()
                .resolve(".skippy")
                .resolve("predictions.log");

        assertThat(readAllLines(predictionsLog, StandardCharsets.UTF_8).toArray()).containsExactlyInAnyOrder(
            "com.example.StringUtilsTest:EXECUTE:NO_COVERAGE_DATA_FOR_TEST"
        );

        var classesMd5Txt = projectDir.toPath()
                .resolve(".skippy")
                .resolve("classes.md5");

        assertThat(readString(classesMd5Txt, StandardCharsets.UTF_8)).isEqualTo("""
            build/classes/java/main:com/example/StringUtils.class:4VP9fWGFUJHKIBG47OXZTQ==
            build/classes/java/test:com/example/StringUtilsTest.class:Bv4JRjxUQd8uU9HGi53p0A==""");

        var stringUtilsTest = projectDir.toPath()
                .resolve(".skippy")
                .resolve("com.example.StringUtilsTest.cov");

        assertThat(readString(stringUtilsTest , StandardCharsets.UTF_8)).isEqualTo("""
            com.example.StringUtils
            com.example.StringUtilsTest
            """);

    }

}