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

package io.skippy.test.maven;

import io.skippy.test.SkippyTestTag;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;

import static java.nio.file.Files.readAllLines;
import static java.nio.file.Files.readString;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Test Impact Analysis using JUnit 5.
 *
 * @author Florian McKee
 */
public class JUnit5SmokeTest {

    @Test
    @Tag(SkippyTestTag.MAVEN)
    public void testSkippyAnalyzeBuild() throws Exception {
        var projectDir = new File(getClass().getResource("/test-projects/junit5-smoketest").toURI());

        var predictionsLog = projectDir.toPath()
                .resolve(".skippy")
                .resolve("predictions.log");

        assertThat(readAllLines(predictionsLog, StandardCharsets.UTF_8).toArray()).containsExactlyInAnyOrder(
         "com.example.LeftPadderTest:EXECUTE:NO_COVERAGE_DATA_FOR_TEST",
            "com.example.RightPadderTest:EXECUTE:NO_COVERAGE_DATA_FOR_TEST"
        );

        var classesMd5Txt = projectDir.toPath()
                .resolve(".skippy")
                .resolve("classes.md5");

        assertThat(readString(classesMd5Txt, StandardCharsets.UTF_8)).isEqualTo("""
            target/classes:com/example/LeftPadder.class:9U3+WYit7uiiNqA9jplN2A==
            target/classes:com/example/RightPadder.class:ZT0GoiWG8Az5TevH9/JwBg==
            target/classes:com/example/StringUtils.class:4VP9fWGFUJHKIBG47OXZTQ==
            target/test-classes:com/example/LeftPadderTest.class:sGLJTZJw4beE9m2Kg6chUg==
            target/test-classes:com/example/RightPadderTest.class:wAwQMlDS3xxmX/Yl5fsSdA==
            target/test-classes:com/example/StringUtilsTest.class:p+N8biKVOm6BltcZkKcC/g==
            target/test-classes:com/example/TestConstants.class:3qNbG+sSd1S1OGe0EZ9GPA==""");

        var leftPadderTestCov = projectDir.toPath()
                .resolve(".skippy")
                .resolve("com.example.LeftPadderTest.cov");

        assertThat(readString(leftPadderTestCov , StandardCharsets.UTF_8)).isEqualTo("""
            com.example.LeftPadder
            com.example.LeftPadderTest
            com.example.StringUtils
            """);

        var rightPadderTestCov = projectDir.toPath()
                .resolve(".skippy")
                .resolve("com.example.RightPadderTest.cov");

        assertThat(readString(rightPadderTestCov , StandardCharsets.UTF_8)).isEqualTo("""
            com.example.RightPadder
            com.example.RightPadderTest
            com.example.StringUtils
            """);
    }

}