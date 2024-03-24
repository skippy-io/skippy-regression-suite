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

import static io.skippy.core.SkippyRegressionTestApi.parseJacocoExecutionDataFiles;
import static io.skippy.core.SkippyRegressionTestApi.parseMergedExecutionDataFiles;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MergeExecutionDataForSkippedTestsTest {

    @Test
    @Tag(SkippyTestTag.MAVEN)
    public void testSkippyAnalyzeBuild() throws Exception {
        var projectDir = new File(getClass().getResource("/test-projects/merge-execution-data-maven").toURI());

        var coveredClasses = parseMergedExecutionDataFiles(projectDir.toPath().resolve("target/skippy.exec"));

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