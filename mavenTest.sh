#!/bin/bash

./gradlew clean
./gradlew :test-projects:junit4-smoketest:skippyClean
./gradlew :test-projects:junit5-smoketest:skippyClean
./gradlew :test-projects:save-execution-data:skippyClean
./gradlew :test-projects:test-failure:skippyClean
./gradlew processTestResources

baseDir=src/test/resources/test-projects

mvn -f $baseDir/junit4-no-skippy-plugin               verify          --no-transfer-progress
mvn -f $baseDir/junit4-smoketest                      verify          --no-transfer-progress
mvn -f $baseDir/junit5-no-skippy-plugin               verify          --no-transfer-progress
mvn -f $baseDir/junit5-smoketest                      verify          --no-transfer-progress
#mvn -f $baseDir/skippy-clean-existing-skippy-folder   skippy:clean    --no-transfer-progress
#mvn -f $baseDir/skippy-clean-no-skippy-folder         skippy:clean    --no-transfer-progress
mvn -f $baseDir/merge-execution-data                   verify          --no-transfer-progress
mvn -f $baseDir/save-execution-data                   verify          --no-transfer-progress
mvn -f $baseDir/test-failure                          verify          --no-transfer-progress

mvn clean test --no-transfer-progress