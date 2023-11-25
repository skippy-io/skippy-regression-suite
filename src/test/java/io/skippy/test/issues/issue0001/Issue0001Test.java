package io.skippy.test.issues.issue0001;

import io.skippy.test.SkippyVersion;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;

import static java.util.regex.Pattern.quote;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class Issue0001Test {

    @Test
    public void testIssue0001() throws Exception {

        var buildFileTemplate = new File(getClass().getResource("build.gradle.template").toURI());
        var projectDir = buildFileTemplate.getParentFile();
        String s = Files.readString(buildFileTemplate.toPath()).replaceAll(quote("${skippyVersion}"), SkippyVersion.VERSION);
        Files.writeString(projectDir.toPath().resolve("build.gradle"), s);

        BuildResult result = GradleRunner.create()
                .withProjectDir(projectDir)
                .withArguments("clean", "skippyClean", "skippyAnalyze")
                .build();

        var output = result.getOutput();

        assertThat(output).contains("""
            No skippified tests found.
            > Task :clean UP-TO-DATE
            > Task :skippyClean
            > Task :skippyAnalyze SKIPPED
                    
            BUILD SUCCESSFUL""");
    }

}
