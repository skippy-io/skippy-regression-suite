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
import org.assertj.core.api.AssertionsForClassTypes;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;

import static io.skippy.core.SkippyRegressionTestApi.parseMergedExecutionDataFiles;
import static io.skippy.test.gradle.Tasks.refresh;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MergeExecutionDataForSkippedTestsTest {

    @Test
    @Tag(SkippyTestTag.GRADLE)
    public void testBuild() throws Exception {
        var projectDir = new File(getClass().getResource("/test-projects/merge-execution-data-gradle").toURI());

        var build = GradleRunner.create()
                .withProjectDir(projectDir)
                .withArguments(refresh("test", "--rerun"))
                .forwardOutput()
                .build();

        var output = build.getOutput();

        assertThat(output).contains("LeftPadderTest > testPadLeft() SKIPPED");
        assertThat(output).contains("RightPadderTest > testPadLeft() SKIPPED");

        var coveredClasses = parseMergedExecutionDataFiles(projectDir.toPath().resolve("build/skippy.exec"));

        assertEquals(
                asList(
                        "com.example.LeftPadder",
                        "com.example.LeftPadderTest",
                        "com.example.RightPadder",
                        "com.example.RightPadderTest",
                        "com.example.StringUtils"
                ),
                coveredClasses.stream().filter(className -> className.startsWith("com.example")).toList()
        );

    }

}