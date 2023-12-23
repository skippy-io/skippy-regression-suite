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

package io.skippy.test.functional;

import io.skippy.test.SkippyVersion;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.lang.System.lineSeparator;
import static java.nio.file.Files.readString;
import static java.util.regex.Pattern.quote;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Functional test for the Getting Started With Gradle & JUnit 5 Tutorial (but with JUnit 5 being replaced with JUnit 4)
 *
 * @author Florian McKee
 */
public class GradleJunit4TutorialTest {

    @Test
    public void testSkippyAnalysisTask() throws Exception {
        var buildFileTemplate = new File(getClass().getResource("gradle_junit4_tutorial/build.gradle.template").toURI());
        var projectDir = buildFileTemplate.getParentFile();
        String buildFile = readString(buildFileTemplate.toPath()).replaceAll(quote("${skippyVersion}"), SkippyVersion.VERSION);
        Files.writeString(projectDir.toPath().resolve("build.gradle"), buildFile);

        BuildResult result = GradleRunner.create()
                .withProjectDir(projectDir)
                .withArguments("skippyAnalyze", "--refresh-dependencies")
                .forwardOutput()
                .build();

        var output = result.getOutput();
        var lines = output.split(lineSeparator());

        assertThat(lines).containsSubsequence(
            "    INFO  i.s.c.SkippyAnalysis - com.example.LeftPadderTest: No coverage data found: Execution required",
            "    INFO  i.s.c.SkippyAnalysis - com.example.RightPadderTest: No coverage data found: Execution required"
        );

        assertThat(lines).containsSubsequence(
            "> Task :skippyAnalyze",
            "Writing skippy/com.example.LeftPadderTest.cov",
            "Writing skippy/com.example.RightPadderTest.cov",
            "Writing skippy/classes.md5"
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