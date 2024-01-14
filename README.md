# skippy-regression-suite

This repo contains a regression suite of functional tests for Skippy itself. This repo is 
- not packaged and published and
- not meant to be used by projects that utilize Skippy.

Unlike regular code that runs at runtime, Skippy operates at the intersection of build & test time. That poses some
challenges for writing high-value unit tests for Skippy. That's the primary reason why Skippy relies heavily on the
functional tests in this repo.

## Contributions & Issues

Contributions are always welcome! You can either
- submit a pull request,
- create an issue in
  [GitHub's issue tracker](https://github.com/skippy-io/skippy-io.github.io/issues) or
- send an email to [contact@skippy.io](mailto:contact@skippy.io).

I would love to hear from you.

## The regression suite

The test suite is split into two parts:
- Regression tests for Gradle-based builds
- Regression tests for Maven-based builds

## Regression tests for Gradle-based builds

The tests for Gradle-based builds work as follows:

1. The projects in `test-projects` are copied to `src/test/resources` (this is triggered by `processTestResources`)
2. The tests in `io.skippy.test.gradle` use Gradle's [TestKit](https://docs.gradle.org/current/userguide/test_kit.html) to build the projects
3. The tests in `io.skippy.test.gradle` then assert against the output from #2

You can run the tests for Gradle-based builds as follows:

```sh
./gradlew gradleTest
```

## Regression tests for Maven-based builds

Unfortunately, there is no Maven counterpart for Gradle's TestKit. That's why the tests for Maven-based builds are 
a little wonky:

1. The projects in `test-projects` are copied to `src/test/resources`
2. The projects in `src/test/resources` are then build on the command line by invoking `mvn` 
3. The tests in `io.skippy.test.maven` then assert against the output from #2

You can run the tests for Maven-based builds as follows:

```sh
./mavenTest.sh
```

## Running the tests via GitHub Actions

A merge to `main` triggers the suite for Gradle and Maven.