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

import static java.nio.file.Files.readString;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Test Impact Analysis without a single test.
 *
 * @author Florian McKee
 */
public class ProjectWithoutTestsTest {

    @Test
    public void testBuild() throws Exception {
        var projectDir = new File(getClass().getResource("/test-projects/project-without-tests").toURI());
        BuildResult result = GradleRunner.create()
                .withProjectDir(projectDir)
                .withArguments("skippyAnalyze", "--refresh-dependencies")
                .build();

        // for troubleshooting purposes
        var output = result.getOutput();

        var predictionsLog = projectDir.toPath().resolve(Path.of("skippy", "predictions.log"));
        assertThat(predictionsLog.toFile().exists()).isFalse();

        var classesMd5Txt = projectDir.toPath().resolve(Path.of("skippy", "classes.md5"));
        assertThat(readString(classesMd5Txt, StandardCharsets.UTF_8)).isEqualTo("""
            build/classes/java/main:com/example/StringUtils.class:4VP9fWGFUJHKIBG47OXZTQ==""");
    }

}