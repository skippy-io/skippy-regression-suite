package io.skippy.test.gradle;

import org.junit.jupiter.params.provider.Arguments;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class GradleVersions {

    static List<String> getAllSupportedVersions() {
        return asList(
                "7.3",
                "7.4",
                "7.5",
                "7.6",
                "8.0",
                "8.1",
                "8.2",
                "8.3",
                "8.4",
                "8.5",
                "8.6",
                "8.7",
                "8.8",
                "8.9",
                "8.10"
                );
    }

    static Stream<Arguments> getAllSupportedVersionsWithConfigurationCacheEnabledAndDisabled() {
        List<Arguments> arguments = new ArrayList<>();
        for (var version : getAllSupportedVersions()) {
            arguments.add(arguments(version, true));
            arguments.add(arguments(version, false));
        }
        return arguments.stream();
    }
}
