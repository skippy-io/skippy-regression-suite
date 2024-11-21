package io.skippy.test.gradle;

import org.junit.jupiter.params.provider.Arguments;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class GradleVersions {
    
    record VersionAndSupportForRerunOption(String version, boolean supportsRerun) {};
    
    static List<VersionAndSupportForRerunOption> getAllSupportedVersions() {
        return asList(
                new VersionAndSupportForRerunOption("7.3", false),
                new VersionAndSupportForRerunOption("7.4", false),
                new VersionAndSupportForRerunOption("7.5", false),
                new VersionAndSupportForRerunOption("7.6", true),
                new VersionAndSupportForRerunOption("8.0", true),
                new VersionAndSupportForRerunOption("8.1", true),
                new VersionAndSupportForRerunOption("8.2", true),
                new VersionAndSupportForRerunOption("8.3", true),
                new VersionAndSupportForRerunOption("8.4", true),
                new VersionAndSupportForRerunOption("8.5", true),
                new VersionAndSupportForRerunOption("8.6", true),
                new VersionAndSupportForRerunOption("8.7", true),
                new VersionAndSupportForRerunOption("8.8", true),
                new VersionAndSupportForRerunOption("8.9", true),
                new VersionAndSupportForRerunOption("8.10", true)
            );
    }

    static Stream<Arguments> getAllSupportedVersionsWithConfigurationCacheEnabledAndDisabled() {
        List<Arguments> arguments = new ArrayList<>();
        for (var version : getAllSupportedVersions()) {
            arguments.add(arguments(version.version, version.supportsRerun, true));
            arguments.add(arguments(version.version, version.supportsRerun, false));
        }
        return arguments.stream();
    }
}
