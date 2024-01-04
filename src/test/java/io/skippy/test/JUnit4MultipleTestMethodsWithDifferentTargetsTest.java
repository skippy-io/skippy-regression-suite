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
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import static java.nio.file.Files.readAllLines;
import static java.nio.file.Files.readString;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Test Impact Analysis using JUnit 4 for a test class with two test methods that invoke different classes.
 *
 * @author Florian McKee
 */
public class JUnit4MultipleTestMethodsWithDifferentTargetsTest {

    @Test
    public void testBuild() throws Exception {
        var buildFileTemplate = new File(getClass().getResource("/projects/junit4-multiple-test-methods-with-different-targets/build.gradle").toURI());
        var projectDir = buildFileTemplate.getParentFile();

        BuildResult result = GradleRunner.create()
                .withProjectDir(projectDir)
                .withArguments("skippyAnalyze", "--refresh-dependencies")
                .build();

        // for troubleshooting purposes
        var output = result.getOutput();

        var leftAndRightPadderTestCov = projectDir.toPath().resolve(Path.of("skippy", "com.example.LeftAndRightPadderTest.cov"));
        assertThat(readString(leftAndRightPadderTestCov , StandardCharsets.UTF_8)).isEqualTo("""
            com.example.LeftAndRightPadderTest
            com.example.LeftPadder
            com.example.RightPadder
            """);
    }

}