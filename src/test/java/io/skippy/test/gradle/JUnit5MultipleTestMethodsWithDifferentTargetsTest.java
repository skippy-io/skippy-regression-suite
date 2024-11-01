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
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static io.skippy.test.gradle.Tasks.refresh;

public class JUnit5MultipleTestMethodsWithDifferentTargetsTest {

    @Test
    @Tag(SkippyTestTag.GRADLE)
    public void testBuild() throws Exception {
        var projectDir = new File(getClass().getResource("/test-projects/junit5-multiple-test-methods-with-different-targets").toURI());
        GradleRunner.create()
                .withProjectDir(projectDir)
                .withArguments(refresh("clean", "skippyClean", "check"))
                .build();

        var tia = Files.readString(projectDir.toPath().resolve(".skippy/test-impact-analysis.json"), StandardCharsets.UTF_8);

        JSONAssert.assertEquals("""
            {
                "classes": {
                    "0": {
                        "name": "com.example.LeftAndRightPadderTest"
                    },
                    "1": {
                        "name": "com.example.LeftPadder"
                    },
                    "2": {
                        "name": "com.example.RightPadder"
                    }
                },
                "tests": [
                    {
                        "class": 0,
                        "tags": ["PASSED"],
                        "coveredClasses": [0,1,2]
                    }
                ]
            }
        """, tia, JSONCompareMode.LENIENT);
    }

}