#!/bin/bash
./gradlew clean processTestResources
mvn -f src/test/resources/test-projects -P sub-modules-only verify -DskippyAnalyze=true
mvn test