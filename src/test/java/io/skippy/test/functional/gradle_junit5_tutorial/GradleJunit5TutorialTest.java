package io.skippy.test.functional.gradle_junit5_tutorial;

import io.skippy.test.SkippyVersion;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;

import static java.util.regex.Pattern.quote;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class GradleJunit5TutorialTest {

    @Test
    public void runTestsWithoutAnalysis() throws Exception {

        var buildFileTemplate = new File(getClass().getResource("build.gradle.template").toURI());
        var projectDir = buildFileTemplate.getParentFile();
        String s = Files.readString(buildFileTemplate.toPath()).replaceAll(quote("${skippyVersion}"), SkippyVersion.VERSION);
        Files.writeString(projectDir.toPath().resolve("build.gradle"), s);

        BuildResult result = GradleRunner.create()
                .withProjectDir(projectDir)
                .withArguments("clean", "skippyClean", "test")
                .build();

        var output = result.getOutput();

        assertThat(output).contains("""             
            > Task :test
                        
            LeftPadderTest STANDARD_OUT
                DEBUG i.s.c.m.SkippyAnalysisResult - com.example.LeftPadderTest: No analysis found. Execution required.
                        
            LeftPadderTest > testPadLeft() PASSED
                        
            RightPadderTest STANDARD_OUT
                DEBUG i.s.c.m.SkippyAnalysisResult - com.example.RightPadderTest: No analysis found. Execution required.
                        
            RightPadderTest > testPadLeft() PASSED
                        
            StringUtilsTest > testPadLeft() PASSED
                        
            StringUtilsTest > testPadRight() PASSED
            """);
    }

    @Test
    public void runTestsWithAnalysis() throws Exception {
        var buildFileTemplate = new File(getClass().getResource("build.gradle.template").toURI());
        var projectDir = buildFileTemplate.getParentFile();
        String s = Files.readString(buildFileTemplate.toPath()).replaceAll(quote("${skippyVersion}"), SkippyVersion.VERSION);
        Files.writeString(projectDir.toPath().resolve("build.gradle"), s);

        BuildResult result = GradleRunner.create()
                .withProjectDir(projectDir)
                .withArguments("clean", "skippyClean", "skippyAnalyze", "test")
                .build();

        var output = result.getOutput();

        assertThat(output).contains("""             
            > Task :skippyClean
                    
            > Task :skippyCoverage_com.example.LeftPadderTest
            Capturing coverage data for com.example.LeftPadderTest in skippy/com.example.LeftPadderTest.csv
                    
            > Task :skippyCoverage_com.example.RightPadderTest
            Capturing coverage data for com.example.RightPadderTest in skippy/com.example.RightPadderTest.csv
                    
            > Task :skippyAnalyze
            Capturing a snapshot of all source files in skippy/sourceSnapshot.md5""");

        assertThat(output).contains("""             
            LeftPadderTest STANDARD_OUT
                DEBUG i.s.c.m.SkippyAnalysisResult - com.example.LeftPadderTest: No changes in test or covered classes detected. Execution skipped.
                        
            LeftPadderTest > testPadLeft() SKIPPED
                        
            RightPadderTest STANDARD_OUT
                DEBUG i.s.c.m.SkippyAnalysisResult - com.example.RightPadderTest: No changes in test or covered classes detected. Execution skipped.
                        
            RightPadderTest > testPadLeft() SKIPPED
                        
            StringUtilsTest > testPadLeft() PASSED
                        
            StringUtilsTest > testPadRight() PASSED""");
    }

}
