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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Disabled
public class JUnit5NoSkippyPluginTest {

    /**
     * This test is just a reminder that the execution of this test is proof that the junit5-no-skippy-plugin
     * build was successful. Otherwise, this test would have never been executed.
     */
    @Test
    @Tag(SkippyTestTag.MAVEN)
    public void testBuild() throws Exception {
        assertTrue(true);
    }

}