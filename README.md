# knimpath

This is the repository containing knimpath, a small port of some functions
found in [Nim's paths
module](https://github.com/nim-lang/Nim/blob/cdfc886f88cc95a8dc05e2211d1030b146a521f5/lib/std/paths.nim)
ported to Kotlin Multiplatform (KMP). Since [Nim is licensed under the MIT
license](https://github.com/nim-lang/Nim/blob/3dda60a8ce32cb7d5e3e99111399a1550c145176/copying.txt)
this *ported* version is [also licensed under the MIT license](LICENSE.md).


# Local publish

    ./gradlew  publishToMavenLocal

# Maven publish

    ./gradlew publishAndReleaseToMavenCentral

# Docs

    ./gradlew :dokkaGenerate && open build/dokka/html/index.html

Use task dokkaHtmlJar to generate a jar for maven distribution.
