package io.skippy.core;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.nio.file.Files.*;
import static java.nio.file.Files.delete;

public class SkippyRegressionTestApi {

    public static void deleteDirectory(Path path) {
        if (Files.isDirectory(path)) {
            try {
                Files.walkFileTree(path, new SimpleFileVisitor<>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        Files.delete(file);
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                        Files.delete(dir);
                        return FileVisitResult.CONTINUE;
                    }
                });
            } catch (IOException e) {
                throw new RuntimeException("Unable to delete directory %s: %s.".formatted(path, e), e);
            }
        }
    }

    public static Map<String, List<String>> parseJacocoExecutionDataFiles(Path projectDir, Path buildDir) {
        var skippyRepo = SkippyRepository.getInstance(new SkippyConfiguration(true, Optional.empty(), Optional.empty()), projectDir, buildDir);
        var testImpactAnalysis = skippyRepo.readLatestTestImpactAnalysis();
        var result = new HashMap<String, List<String>>();
        for (var analyzedTest : testImpactAnalysis.getAnalyzedTests()) {
            var testClass = testImpactAnalysis.getClassFileContainer().getById(analyzedTest.getTestClassId());
            var coveredClasses = JacocoUtil.getCoveredClasses(skippyRepo.readJacocoExecutionData(analyzedTest.getExecutionId().get()).get());
            result.put(testClass.getClassName(), coveredClasses.stream().map(ClassNameAndJaCoCoId::className).toList());
        }
        return result;
    }

    public static List<String> parseExecutionDataFile(Path execFile) {
        try {
            var execData = Files.readAllBytes(execFile);
            return JacocoUtil.getCoveredClasses(execData).stream()
                    .map(ClassNameAndJaCoCoId::className)
                    .toList();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
