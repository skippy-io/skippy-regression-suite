#!/bin/bash

./gradlew processTestResources

baseDir=src/test/resources/test-projects

mvn -f $baseDir/junit5-smoketest                      verify -DskippyAnalyze=true --no-transfer-progress
mvn -f $baseDir/junit5-no-skippy-plugin               verify -DskippyAnalyze=true --no-transfer-progress
mvn -f $baseDir/junit4-smoketest                      verify -DskippyAnalyze=true --no-transfer-progress
mvn -f $baseDir/junit4-no-skippy-plugin               verify -DskippyAnalyze=true --no-transfer-progress
mvn -f $baseDir/skippy-clean-existing-skippy-folder   skippy:clean                --no-transfer-progress
mvn -f $baseDir/skippy-clean-no-skippy-folder         skippy:clean                --no-transfer-progress

mvn clean test --no-transfer-progress