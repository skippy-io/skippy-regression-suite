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
import org.gradle.testkit.runner.GradleRunner;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Disabled
public class SkippyCleanExistingSkippyFolderTest {

    @Test
    @Tag(SkippyTestTag.GRADLE)
    public void testBuild() throws Exception {
        var projectDir = new File(getClass().getResource("/test-projects/skippy-clean-existing-skippy-folder").toURI());

        assertEquals(true, projectDir.toPath().resolve(".skippy").toFile().exists());

        GradleRunner.create()
                .withProjectDir(projectDir)
                .withArguments("skippyClean", "--refresh-dependencies")
                .build();

        assertEquals(false, projectDir.toPath().resolve(".skippy").toFile().exists());
    }

}