#!/bin/bash
./gradlew clean processTestResources
mvn -f src/test/resources/test-projects verify -DskippyAnalyze=true -P sub-modules-only --no-transfer-progress
mvn test --no-transfer-progress