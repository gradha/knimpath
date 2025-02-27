# Version number (tags')

Modify `shared/build.gradle.kts`.

# Local publish

    ./gradlew  publishToMavenLocal

# Docs

    ./gradlew dokkaHtml && open shared/build/dokka/html/index.html

Use task dokkaHtmlJar to generate a jar for maven distribution.
