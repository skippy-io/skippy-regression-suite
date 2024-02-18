package io.skippy.test.gradle;

import org.apache.groovy.util.Arrays;

class Tasks {

    private static final boolean REFRESH_DEPENDENCIES = false;

    static String[] refresh(String... tasks) {
        if (false == REFRESH_DEPENDENCIES) {
            return tasks;
        }
        return Arrays.concat(
                tasks,
                new String[] {
                        "--refresh-dependencies"
                }
        );
    }

}
