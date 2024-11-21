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
import static java.nio.file.Files.readAllLines;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class JUnit5DuplicateClassNamesTest {

    @Test
    @Tag(SkippyTestTag.GRADLE)
    public void testBuild() throws Exception {
        var projectDir = new File(getClass().getResource("/test-projects/junit5-duplicate-class-names").toURI());
        GradleRunner.create()
                .withProjectDir(projectDir)
                .withArguments(refresh("clean", "skippyClean", "check"))
                .build();

        var tia = Files.readString(projectDir.toPath().resolve(".skippy/test-impact-analysis.json"), StandardCharsets.UTF_8);

        JSONAssert.assertEquals("""
            {
                "classes": {
                    "0": {
                        "name": "com.example.Bar",
                        "outputFolder": "build/classes/java/debugTest"
                    },
                    "1": {
                        "name": "com.example.Bar",
                        "outputFolder": "build/classes/java/main"
                    },
                    "2": {
                        "name": "com.example.Bar",
                        "outputFolder": "build/classes/java/release"
                    },
                    "3": {
                        "name": "com.example.Bar",
                        "outputFolder": "build/classes/java/releaseTest"
                    },
                    "4": {
                        "name": "com.example.BarTest",
                        "outputFolder": "build/classes/java/debugTest"
                    },
                    "5": {
                        "name": "com.example.BarTest",
                        "outputFolder": "build/classes/java/releaseTest"
                    },
                    "6": {
                        "name": "com.example.BarTest",
                        "outputFolder": "build/classes/java/test"
                    },
                    "7": {
                        "name": "com.example.Foo",
                        "outputFolder": "build/classes/java/debug"
                    },
                    "8": {
                        "name": "com.example.Foo",
                        "outputFolder": "build/classes/java/main"
                    },
                    "9": {
                        "name": "com.example.Foo",
                        "outputFolder": "build/classes/java/release"
                    },
                    "10": {
                        "name": "com.example.FooTest",
                        "outputFolder": "build/classes/java/debugTest"
                    },
                    "11": {
                        "name": "com.example.FooTest",
                        "outputFolder": "build/classes/java/releaseTest"
                    },
                    "12": {
                        "name": "com.example.FooTest",
                        "outputFolder": "build/classes/java/test"
                    }
                },
                "tests": [
                    {
                        "class": 4,
                        "coveredClasses": [0,4]
                    },
                    {
                        "class": 5,
                        "coveredClasses": [3,5]
                    },
                    {
                        "class": 6,
                        "coveredClasses": [1,6]
                    },
                    {
                        "class": 10,
                        "coveredClasses": [7,10]
                    },
                    {
                        "class": 11,
                        "coveredClasses": [9,11]
                    },
                    {
                        "class": 12,
                        "coveredClasses": [8,12]
                    }
                ]
            }
        """, tia, JSONCompareMode.LENIENT);


        GradleRunner.create()
                .withProjectDir(projectDir)
                .withArguments(refresh("check", "--rerun-tasks"))
                .forwardOutput()
                .build();

        var loggingLog = projectDir.toPath().resolve(".skippy").resolve("logging.log");
        assertThat(readAllLines(loggingLog, StandardCharsets.UTF_8).toArray()).containsExactlyInAnyOrder(
                "Mapping class build/classes/java/debugTest/com.example.BarTest to AnalyzedTest[build/classes/java/debugTest/com.example.BarTest]",
                "Mapping class build/classes/java/debugTest/com.example.FooTest to AnalyzedTest[build/classes/java/debugTest/com.example.FooTest]",
                "Mapping class build/classes/java/releaseTest/com.example.BarTest to AnalyzedTest[build/classes/java/releaseTest/com.example.BarTest]",
                "Mapping class build/classes/java/releaseTest/com.example.FooTest to AnalyzedTest[build/classes/java/releaseTest/com.example.FooTest]",
                "Mapping class build/classes/java/test/com.example.BarTest to AnalyzedTest[build/classes/java/test/com.example.BarTest]",
                "Mapping class build/classes/java/test/com.example.FooTest to AnalyzedTest[build/classes/java/test/com.example.FooTest]"
        );
    }

}