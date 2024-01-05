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

package io.skippy.test;

import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
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
 * Test Impact Analysis using multiple versions of JUnit 4
 *
 * @author Florian McKee
 */

public class JUnit4CompatibilityTest {

    @ParameterizedTest
    @ValueSource(strings = {"4.10", "4.11", "4.13", "4.13.1", "4.13.2"})
    public void testBuild(String junit5Version) throws Exception {
        var projectDir = new File(getClass().getResource("/test-projects/junit4-compatibility").toURI());
        BuildResult result = GradleRunner.create()
                .withProjectDir(projectDir)
                .withEnvironment(Map.of("junit4Version", junit5Version))
                .withArguments("dependencies", "skippyAnalyze", "--refresh-dependencies")
                .forwardOutput()
                .build();

        // for troubleshooting purposes
        var output = result.getOutput();

        var predictionsLog = projectDir.toPath().resolve(Path.of("skippy", "predictions.log"));
        assertThat(readAllLines(predictionsLog, StandardCharsets.UTF_8).toArray()).containsExactlyInAnyOrder(
                "com.example.LeftPadderTest:EXECUTE:NO_COVERAGE_DATA_FOR_TEST",
                "com.example.RightPadderTest:EXECUTE:NO_COVERAGE_DATA_FOR_TEST"
        );

        var classesMd5Txt = projectDir.toPath().resolve(Path.of("skippy", "classes.md5"));
        assertThat(readString(classesMd5Txt, StandardCharsets.UTF_8)).isEqualTo("""
            build/classes/java/main:com/example/LeftPadder.class:9U3+WYit7uiiNqA9jplN2A==
            build/classes/java/main:com/example/RightPadder.class:ZT0GoiWG8Az5TevH9/JwBg==
            build/classes/java/main:com/example/StringUtils.class:4VP9fWGFUJHKIBG47OXZTQ==
            build/classes/java/test:com/example/LeftPadderTest.class:PfiMSJHtPoujnc6hlyYayA==
            build/classes/java/test:com/example/RightPadderTest.class:0RaVJ4PjsVSzBTC0Mgey8g==
            build/classes/java/test:com/example/StringUtilsTest.class:rURYgK6CQqdn6cutCLdqqQ==
            build/classes/java/test:com/example/TestConstants.class:3qNbG+sSd1S1OGe0EZ9GPA==""");

        var leftPadderTestCov = projectDir.toPath().resolve(Path.of("skippy", "com.example.LeftPadderTest.cov"));
        assertThat(readString(leftPadderTestCov , StandardCharsets.UTF_8)).isEqualTo("""
            com.example.LeftPadder
            com.example.LeftPadderTest
            com.example.StringUtils
            """);

        var rightPadderTestCov = projectDir.toPath().resolve(Path.of("skippy", "com.example.RightPadderTest.cov"));
        assertThat(readString(rightPadderTestCov , StandardCharsets.UTF_8)).isEqualTo("""
            com.example.RightPadder
            com.example.RightPadderTest
            com.example.StringUtils
            """);
    }

}