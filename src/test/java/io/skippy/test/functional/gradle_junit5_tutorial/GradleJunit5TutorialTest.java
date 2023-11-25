package io.skippy.test.functional.gradle_junit5_tutorial;

import io.skippy.test.SkippyVersion;
import org.gradle.api.BuildCancelledException;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

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


        var snapshotMd5File = projectDir.toPath().resolve(Path.of("skippy", "sourceSnapshot.md5"));
        var snapshotMd5Content = Files
                .readString(snapshotMd5File)
                .replaceAll(projectDir.toString() + "/src/main/java/", "")
                .replaceAll(projectDir.toString() + "/src/test/java/", "")
                .replaceAll(projectDir.toString() + "/build/classes/java/main/", "")
                .replaceAll(projectDir.toString() + "/build/classes/java/test/", "");

//        assertThat(snapshotMd5Content).contains("""
//                com.example.StringUtils:com/example/StringUtils.java:com/example/StringUtils.class:OUit8FjiK8bRBHkjssO9+Q==:TB3Ri7NR47VGzsGKfSF6cg==
//                com.example.RightPadder:com/example/RightPadder.java:com/example/RightPadder.class:lbQRvgnICPwJcg0ObY2wfA==:FgPLN2IwhX2Y1n7TLYG9aw==
//                com.example.LeftPadder:com/example/LeftPadder.java:com/example/LeftPadder.class:99PUNZm+uo4Rp5feNB5d/g==:HeDsMUqerZxYhOi8+SyxHA==
//                com.example.TestConstants:com/example/TestConstants.java:com/example/TestConstants.class:nK/HNeYLMeGZk5hlcPS8Yg==:CjlZNllkdXvp5RozTW9ycQ==
//                com.example.LeftPadderTest:com/example/LeftPadderTest.java:com/example/LeftPadderTest.class:tmeyvGT5uJAMQyQzbqbvyg==:zEb0x7PQhzYAh00yZX50Wg==
//                com.example.RightPadderTest:com/example/RightPadderTest.java:com/example/RightPadderTest.class:LfOMUnHmz0Gqv48PyG+Arw==:pfL18c7B6SOZiFB+TsHpaw==
//                com.example.StringUtilsTest:com/example/StringUtilsTest.java:com/example/StringUtilsTest.class:yq8CHRvmLIB5vb/eqkOlIw==:KJg84+nME0Yh7uBsXwv9Vg==""");

        var leftPadderTestCsvFile = projectDir.toPath().resolve(Path.of("skippy", "com.example.LeftPadderTest.csv"));
        var leftPadderTestCsv = Files.readString(leftPadderTestCsvFile);

        assertThat(leftPadderTestCsv).contains("""
            GROUP,PACKAGE,CLASS,INSTRUCTION_MISSED,INSTRUCTION_COVERED,BRANCH_MISSED,BRANCH_COVERED,LINE_MISSED,LINE_COVERED,COMPLEXITY_MISSED,COMPLEXITY_COVERED,METHOD_MISSED,METHOD_COVERED
            gradle_junit5_tutorial,com.example,TestConstants,3,0,0,0,1,0,1,0,1,0
            gradle_junit5_tutorial,com.example,StringUtils,14,11,2,2,4,3,3,2,2,1
            gradle_junit5_tutorial,com.example,LeftPadder,3,4,0,0,1,1,1,1,1,1
            gradle_junit5_tutorial,com.example,RightPadder,7,0,0,0,2,0,2,0,2,0
            gradle_junit5_tutorial,com.example,RightPadderTest,11,0,0,0,4,0,2,0,2,0
            gradle_junit5_tutorial,com.example,LeftPadderTest,0,11,0,0,0,4,0,2,0,2
            gradle_junit5_tutorial,com.example,StringUtilsTest,19,0,0,0,7,0,3,0,3,0""");

        var rightPadderTestCsvFile = projectDir.toPath().resolve(Path.of("skippy", "com.example.RightPadderTest.csv"));
        var rightPadderTestCsv = Files.readString(rightPadderTestCsvFile);

        assertThat(rightPadderTestCsv).contains("""
            GROUP,PACKAGE,CLASS,INSTRUCTION_MISSED,INSTRUCTION_COVERED,BRANCH_MISSED,BRANCH_COVERED,LINE_MISSED,LINE_COVERED,COMPLEXITY_MISSED,COMPLEXITY_COVERED,METHOD_MISSED,METHOD_COVERED
            gradle_junit5_tutorial,com.example,TestConstants,3,0,0,0,1,0,1,0,1,0
            gradle_junit5_tutorial,com.example,StringUtils,14,11,2,2,4,3,3,2,2,1
            gradle_junit5_tutorial,com.example,LeftPadder,7,0,0,0,2,0,2,0,2,0
            gradle_junit5_tutorial,com.example,RightPadder,3,4,0,0,1,1,1,1,1,1
            gradle_junit5_tutorial,com.example,RightPadderTest,0,11,0,0,0,4,0,2,0,2
            gradle_junit5_tutorial,com.example,LeftPadderTest,11,0,0,0,4,0,2,0,2,0
            gradle_junit5_tutorial,com.example,StringUtilsTest,19,0,0,0,7,0,3,0,3,0""");
    }

}
