package io.skippy.core;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SkippyRegressionTestApi {

    public static Map<String, List<String>> parseJacocoExecutionDataFiles(Path projectDir, Path buildDir) {
        var skippyRepo = SkippyRepository.getInstance(new SkippyConfiguration(true), projectDir, buildDir);
        var testImpactAnalysis = skippyRepo.readTestImpactAnalysis().get();
        var result = new HashMap<String, List<String>>();
        for (var analyzedTest : testImpactAnalysis.getAnalyzedTests()) {
            var testClass = testImpactAnalysis.getClassFileContainer().getById(analyzedTest.getTestClassId());
            result.put(testClass.getClassName(), JacocoUtil.getCoveredClasses(skippyRepo.readJacocoExecutionData(analyzedTest.getExecutionId().get()).get()));
        }
        return result;
    }

    public static List<String> parseMergedExecutionDataFiles(Path execFile) {
        try {
            var execData = Files.readAllBytes(execFile);
            return JacocoUtil.getCoveredClasses(execData);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
