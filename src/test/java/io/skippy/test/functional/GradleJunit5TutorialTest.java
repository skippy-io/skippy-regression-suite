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
import java.nio.file.Files;
import java.nio.file.Path;

import static java.lang.System.lineSeparator;
import static java.nio.file.Files.readAllLines;
import static java.util.regex.Pattern.quote;
import static java.util.stream.Collectors.joining;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Functional test for the Getting Started With Gradle & JUnit 5 Tutorial.
 *
 * @author Florian McKee
 */
public class GradleJunit5TutorialTest {

    @Test
    public void runTestsWithoutAnalysis() throws Exception {

        var buildFileTemplate = new File(getClass().getResource("gradle_junit5_tutorial/build.gradle.template").toURI());
        var projectDir = buildFileTemplate.getParentFile();
        String buildFile = Files.readString(buildFileTemplate.toPath()).replaceAll(quote("${skippyVersion}"), SkippyVersion.VERSION);
        Files.writeString(projectDir.toPath().resolve("build.gradle"), buildFile);

        BuildResult result = GradleRunner.create()
                .withProjectDir(projectDir)
                .withDebug(true)
                .withArguments("clean", "skippyClean", "test")
                .forwardOutput()
                .build();

        var output = result.getOutput();
        var lines = output.split(lineSeparator());

        assertThat(lines).containsSubsequence(
            "> Task :test",

            "    DEBUG i.s.c.SkippyAnalysis - com.example.LeftPadderTest: No analysis found. Execution required.",
            "LeftPadderTest > testPadLeft() PASSED",

            "    DEBUG i.s.c.SkippyAnalysis - com.example.RightPadderTest: No analysis found. Execution required.",
            "RightPadderTest > testPadLeft() PASSED",
                        
            "StringUtilsTest > testPadLeft() PASSED",
            "StringUtilsTest > testPadRight() PASSED"
        );
    }

    @Test
    public void runTestsWithAnalysis() throws Exception {
        var buildFileTemplate = new File(getClass().getResource("gradle_junit5_tutorial/build.gradle.template").toURI());
        var projectDir = buildFileTemplate.getParentFile();
        String buildFile = Files.readString(buildFileTemplate.toPath()).replaceAll(quote("${skippyVersion}"), SkippyVersion.VERSION);
        Files.writeString(projectDir.toPath().resolve("build.gradle"), buildFile);

        BuildResult result = GradleRunner.create()
                .withProjectDir(projectDir)
                .withDebug(true)
                .withArguments("clean", "skippyAnalyze", "test")
                .forwardOutput()
                .build();

        var output = result.getOutput();
        var lines = output.split(lineSeparator());

        assertThat(lines).containsSubsequence(
            "> Task :skippyClean",
            "> Task :skippyAnalyze",
            "com.example.LeftPadderTest > Capturing coverage data in skippy/com.example.LeftPadderTest.csv",
            "com.example.RightPadderTest > Capturing coverage data in skippy/com.example.RightPadderTest.csv",
            "Creating the Skippy analysis file skippy/analyzedFiles.txt."
        );

        assertThat(lines).containsSubsequence(
            "    DEBUG i.s.c.SkippyAnalysis - com.example.LeftPadderTest: No changes in test or covered classes detected. Execution skipped.",
            "LeftPadderTest > testPadLeft() SKIPPED",

            "    DEBUG i.s.c.SkippyAnalysis - com.example.RightPadderTest: No changes in test or covered classes detected. Execution skipped.",
            "RightPadderTest > testPadLeft() SKIPPED"
        );


        var analyzedFilesTxt = projectDir.toPath().resolve(Path.of("skippy", "analyzedFiles.txt"));
        var analyzedFilesTxtContent = readAllLines(analyzedFilesTxt).stream().sorted().collect(joining(lineSeparator()));

        assertThat(analyzedFilesTxtContent).contains("""
                build/classes/java/main/com/example/LeftPadder.class:9U3+WYit7uiiNqA9jplN2A==
                build/classes/java/main/com/example/RightPadder.class:ZT0GoiWG8Az5TevH9/JwBg==
                build/classes/java/main/com/example/StringUtils.class:4VP9fWGFUJHKIBG47OXZTQ==
                build/classes/java/test/com/example/LeftPadderTest.class:3KxzE+CKm6BJ3KetctvnNA==
                build/classes/java/test/com/example/RightPadderTest.class:naR4eGh3LU+eDNSQXvsIyw==
                build/classes/java/test/com/example/StringUtilsTest.class:p+N8biKVOm6BltcZkKcC/g==
                build/classes/java/test/com/example/TestConstants.class:3qNbG+sSd1S1OGe0EZ9GPA==""");

        var leftPadderTestCsvFile = projectDir.toPath().resolve(Path.of("skippy", "com.example.LeftPadderTest.csv"));
        var leftPadderTestCsv = readAllLines(leftPadderTestCsvFile).stream().sorted().collect(joining(lineSeparator()));

        assertThat(leftPadderTestCsv).contains("""
            GROUP,PACKAGE,CLASS,INSTRUCTION_MISSED,INSTRUCTION_COVERED,BRANCH_MISSED,BRANCH_COVERED,LINE_MISSED,LINE_COVERED,COMPLEXITY_MISSED,COMPLEXITY_COVERED,METHOD_MISSED,METHOD_COVERED
            gradle_junit5_tutorial,com.example,LeftPadder,3,4,0,0,1,1,1,1,1,1
            gradle_junit5_tutorial,com.example,LeftPadderTest,0,11,0,0,0,4,0,2,0,2
            gradle_junit5_tutorial,com.example,RightPadder,7,0,0,0,2,0,2,0,2,0
            gradle_junit5_tutorial,com.example,RightPadderTest,11,0,0,0,4,0,2,0,2,0
            gradle_junit5_tutorial,com.example,StringUtils,14,11,2,2,4,3,3,2,2,1
            gradle_junit5_tutorial,com.example,StringUtilsTest,19,0,0,0,7,0,3,0,3,0
            gradle_junit5_tutorial,com.example,TestConstants,3,0,0,0,1,0,1,0,1,0""");

        var rightPadderTestCsvFile = projectDir.toPath().resolve(Path.of("skippy", "com.example.RightPadderTest.csv"));
        var rightPadderTestCsv = readAllLines(rightPadderTestCsvFile).stream().sorted().collect(joining(lineSeparator()));

        assertThat(rightPadderTestCsv).contains("""
            GROUP,PACKAGE,CLASS,INSTRUCTION_MISSED,INSTRUCTION_COVERED,BRANCH_MISSED,BRANCH_COVERED,LINE_MISSED,LINE_COVERED,COMPLEXITY_MISSED,COMPLEXITY_COVERED,METHOD_MISSED,METHOD_COVERED
            gradle_junit5_tutorial,com.example,LeftPadder,7,0,0,0,2,0,2,0,2,0
            gradle_junit5_tutorial,com.example,LeftPadderTest,11,0,0,0,4,0,2,0,2,0
            gradle_junit5_tutorial,com.example,RightPadder,3,4,0,0,1,1,1,1,1,1
            gradle_junit5_tutorial,com.example,RightPadderTest,0,11,0,0,0,4,0,2,0,2
            gradle_junit5_tutorial,com.example,StringUtils,14,11,2,2,4,3,3,2,2,1
            gradle_junit5_tutorial,com.example,StringUtilsTest,19,0,0,0,7,0,3,0,3,0
            gradle_junit5_tutorial,com.example,TestConstants,3,0,0,0,1,0,1,0,1,0""");
    }

}