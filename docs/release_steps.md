# knimpath release steps

Release steps for [knimpath](https://github.com/gradha/knimpath). See the [README](../README.md).

* Pick a release version number ending in even number (eg. `1.4.2`)
* Create branch `release_1.4.2`.
* Run `./gradlew allTests` to verify at least basic stuff works.
* Modify [shared/build.gradle.kts](../shared/build.gradle.kts) and modify
  `version` to match release number.
* ``git commit -av`` into the release branch the version number changes.
* ``git tag v0.1.4`` to mark the release.
* Run ``./extra/publishToMavenLocal.sh`` to build and sign release locally.
* Run ``./extra/publishAndReleaseToMavenCentral.sh`` to build and sign
  for maven central.
* Modify [shared/build.gradle.kts](../shared/build.gradle.kts) and modify
  `version` to increment release number for next development version.
* ``git commit -av`` into the release branch and merge with main.
