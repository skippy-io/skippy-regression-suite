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
 * Test Impact Analysis using a project with multiple tests SourceSets and JUnit 4 and JUnit 5.
 *
 * @author Florian McKee
 */
public class MultipleJUnit5ExtensionsTest {

    @Test
    public void testBuild() throws Exception {
        var projectDir = new File(getClass().getResource("/test-projects/multiple-junit5-extensions").toURI());
        BuildResult result = GradleRunner.create()
                .withProjectDir(projectDir)
                .withArguments("skippyAnalyze", "--refresh-dependencies")
                .forwardOutput()
                .build();

        var output = result.getOutput();

        assertThat(output).contains("ExtensionThatShouldNotBeExecuted was executed");

        var classesMd5Txt = projectDir.toPath().resolve(Path.of("skippy", "classes.md5"));
        assertThat(readString(classesMd5Txt, StandardCharsets.UTF_8)).isEqualTo("""
            build/classes/java/test:com/example/ExtensionThatShouldNotBeExecuted.class:0Zx5kZ1vwuaelNWDTvqB3g==
            build/classes/java/test:com/example/FooTest.class:1cNbXny4CIjPi+2kNqlXAg==""");

        var fooTestCov = projectDir.toPath().resolve(Path.of("skippy", "com.example.FooTest.cov"));
        assertThat(readString(fooTestCov, StandardCharsets.UTF_8)).isEqualTo("""
            com.example.ExtensionThatShouldNotBeExecuted
            com.example.FooTest
            """);

        var predictionsLog = projectDir.toPath().resolve(Path.of("skippy", "predictions.log"));
        assertThat(readAllLines(predictionsLog, StandardCharsets.UTF_8).toArray()).containsExactlyInAnyOrder(
            "com.example.FooTest:EXECUTE:NO_COVERAGE_DATA_FOR_TEST"
        );

        result = GradleRunner.create()
                .withProjectDir(projectDir)
                .withArguments("check", "--refresh-dependencies")
                .forwardOutput()
                .build();

        output = result.getOutput();

        assertThat(output).doesNotContain("ExtensionThatShouldNotBeExecuted was executed");

        predictionsLog = projectDir.toPath().resolve(Path.of("skippy", "predictions.log"));
        assertThat(readAllLines(predictionsLog, StandardCharsets.UTF_8).toArray()).containsExactlyInAnyOrder(
                "com.example.FooTest:SKIP:NO_CHANGE"
        );


    }

}