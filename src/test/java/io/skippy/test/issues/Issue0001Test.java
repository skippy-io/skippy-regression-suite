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

package io.skippy.test.issues;

import io.skippy.test.SkippyVersion;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;

import static java.lang.System.lineSeparator;
import static java.util.regex.Pattern.quote;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Functional test for https://github.com/skippy-io/skippy/issues/1.
 *
 * @author Florian McKee
 */
public class Issue0001Test {

    @Test
    public void testIssue0001() throws Exception {

        var buildFileTemplate = new File(getClass().getResource("issue0001/build.gradle.template").toURI());
        var projectDir = buildFileTemplate.getParentFile();
        String buildFile = Files.readString(buildFileTemplate.toPath()).replaceAll(quote("${skippyVersion}"), SkippyVersion.VERSION);
        Files.writeString(projectDir.toPath().resolve("build.gradle"), buildFile);

        BuildResult result = GradleRunner.create()
                .withProjectDir(projectDir)
                .withArguments("clean", "skippyClean", "skippyAnalyze")
                .forwardOutput()
                .build();

        var output = result.getOutput();
        var lines = output.split(lineSeparator());

        assertThat(lines).containsSubsequence(
            "> Task :skippyAnalyze",
            "Creating the Skippy analysis file skippy/analyzedFiles.txt."
        );
    }

}
