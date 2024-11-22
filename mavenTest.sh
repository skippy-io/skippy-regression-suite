#!/bin/bash

./gradlew clean
./gradlew :test-projects:custom-repository:skippyClean
./gradlew :test-projects:custom-prediction-modifier:skippyClean
./gradlew :test-projects:junit4-smoketest:skippyClean
./gradlew :test-projects:junit4-test-failure:skippyClean
./gradlew :test-projects:junit5-always-run-annotation:skippyClean
./gradlew :test-projects:junit5-always-run-annotation-nested-tests:skippyClean
./gradlew :test-projects:junit5-nested-tests:skippyClean
./gradlew :test-projects:junit5-smoketest:skippyClean
./gradlew :test-projects:junit5-test-failure:skippyClean
./gradlew :test-projects:save-execution-data:skippyClean
./gradlew processTestResources

baseDir=src/test/resources/test-projects

rm -rf $baseDir/custom-repository/.skippy
rm -rf $baseDir/custom-prediction-modifier/.skippy
rm -rf $baseDir/junit4-smoketest/.skippy
rm -rf $baseDir/junit4-test-failure/.skippy
rm -rf $baseDir/save-execution-data/.skippy
rm -rf $baseDir/junit5-test-failure/.skippy

mvn -f $baseDir/custom-repository                         verify              --no-transfer-progress
mvn -f $baseDir/custom-prediction-modifier                verify              --no-transfer-progress
mvn -f $baseDir/junit4-no-skippy-plugin                   verify              --no-transfer-progress
mvn -f $baseDir/junit4-test-failure                       verify              --no-transfer-progress
mvn -f $baseDir/junit4-smoketest                          verify              --no-transfer-progress
mvn -f $baseDir/junit5-no-skippy-plugin                   verify              --no-transfer-progress
mvn -f $baseDir/junit5-always-run-annotation              verify              --no-transfer-progress
mvn -f $baseDir/junit5-always-run-annotation-nested-tests verify              --no-transfer-progress
mvn -f $baseDir/junit5-nested-tests                       verify              --no-transfer-progress
mvn -f $baseDir/junit5-smoketest                          verify              --no-transfer-progress
mvn -f $baseDir/junit5-test-failure                       verify              --no-transfer-progress

mvn -f $baseDir/skippy-clean-existing-skippy-folder       skippy:clean        --no-transfer-progress
mvn -f $baseDir/skippy-clean-no-skippy-folder             skippy:clean        --no-transfer-progress
mvn -f $baseDir/merge-execution-data-maven                verify              --no-transfer-progress
mvn -f $baseDir/save-execution-data                       verify              --no-transfer-progress

mvn clean test --no-transfer-progress