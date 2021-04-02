# Micronaut XXX

[![Maven Central](https://img.shields.io/maven-central/v/io.micronaut.problem/micronaut-problem.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.micronaut.problem%22%20AND%20a:%22micronaut-problem%22)
[![Build Status](https://github.com/micronaut-projects/micronaut-problem/workflows/Java%20CI/badge.svg)](https://github.com/micronaut-projects/micronaut-problem/actions)

Micronaut XXX is...

## Documentation

See the [Documentation](https://micronaut-projects.github.io/micronaut-problem/latest/guide/) for more information. 

See the [Snapshot Documentation](https://micronaut-projects.github.io/micronaut-problem/snapshot/guide/) for the current development docs.

## Snapshots and Releases

Snaphots are automatically published to [Sonatype Snapshots](https://oss.sonatype.org/content/repositories/snapshots/) using [Github Actions](https://github.com/micronaut-projects/micronaut-problem/actions).

See the documentation in the [Micronaut Docs](https://docs.micronaut.io/latest/guide/index.html#usingsnapshots) for how to configure your build to use snapshots.

Releases are published to Maven Central via [Github Actions](https://github.com/micronaut-projects/micronaut-problem/actions).

Releases are completely automated. To perform a release use the following steps:

* [Publish the draft release](https://github.com/micronaut-projects/micronaut-problem/releases). There should be already a draft release created, edit and publish it. The Git Tag should start with `v`. For example `v1.0.0`.
* [Monitor the Workflow](https://github.com/micronaut-projects/micronaut-problem/actions?query=workflow%3ARelease) to check it passed successfully.
* If everything went fine, [publish to Maven Central](https://github.com/micronaut-projects/micronaut-problem/actions?query=workflow%3A"Maven+Central+Sync").
* Celebrate!
