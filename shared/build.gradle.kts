import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.apache.tools.ant.taskdefs.condition.Os
import kotlin.jvm.java

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    //alias(libs.plugins.androidLibrary)
    alias(libs.plugins.vannipublish)
    id("maven-publish")
}

kotlin {
    /*
    androidTarget {
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_1_8)
                }
            }
        }
    }
     */

    macosX64()
    macosArm64()
    mingwX64()

    /*
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }
     */

    sourceSets {
        commonMain.dependencies {
            //put your multiplatform dependencies here
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        macosMain.dependencies {
            // Add native dependencies here. Example:
            // api(platform("org.jetbrains.kotlinx:kotlinx-coroutines-core-macosx:1.6.4"))
        }
    }
    withSourcesJar()
}

/*
android {
    namespace = "es.elhaso.knimpath"
    compileSdk = 34
    defaultConfig {
        minSdk = 21
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}
 */

group = "es.elhaso.knimpath"
version = "0.1.4"

publishing {
    repositories {
        maven {
            // https://stackoverflow.com/a/71176846/172690
            name = "localDist-knimpath"
            url = uri(layout.buildDirectory.dir("localDist"))
        }
    }
}

// https://github.com/Kotlin/multiplatform-library-template
mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    val isRelease = if (extra.has("isRelease")) extra.get("isRelease") as? String else null
    if (isRelease == "true")
        signAllPublications()

    coordinates(group.toString(), "knimpath", version.toString())

    pom {
        name = "Kotlin version of Nim paths procs"
        description = "Small port of some functions found in Nim's paths module ported to KMP."
        inceptionYear = "2025"
        url = "https://github.com/gradha/knimpath"
        licenses {
            license {
                name = "The MIT License"
                url = "https://opensource.org/license/mit"
                distribution = "https://opensource.org/license/mit"
            }
        }
        developers {
            developer {
                id = "gradha"
                name = "Grzegorz Adam Hankiewicz"
                url = "https://github.com/gradha"
            }
        }
        scm {
            url = "https://github.com/gradha/knimpath"
            connection = "scm:git:git://github.com/gradha/knimpath.git"
            developerConnection = "scm:git:ssh://git@github.com/gradha/knimpath.git"
        }
    }
}

// build.gradle.kts

dokka {
    moduleName.set("knimpath")
    dokkaPublications.html {
        suppressInheritedMembers.set(true)
        failOnWarning.set(true)
    }
    dokkaSourceSets.commonMain {
        //includes.from("README.md")
        /*
        sourceLink {
            localDirectory.set(file("src/main/kotlin"))
            remoteUrl("https://example.com/src")
            remoteLineSuffix.set("#L")
        }
         */
    }
    pluginsConfiguration.html {
        //customStyleSheets.from("styles.css")
        //customAssets.from("logo.png")
        //footerMessage.set("(c) Electric Hands Software")
    }
}


val exclusions = listOf(".git", "build", ".gradle", "gradle")

// Function to define the archive base name
fun archiveBaseName(): String {
    val projectName = project.name.replace(Regex("[^a-zA-Z0-9]"), "-")
    return "knimpath-${project.version}"
}

// Common configuration for archive tasks
fun configureArchiveTask(task: org.gradle.api.tasks.bundling.AbstractArchiveTask) {
    task.apply {
        archiveBaseName.set(archiveBaseName())
        destinationDirectory.set(file("${project.buildDir}/distributions"))
        // Filter out unwanted files
        duplicatesStrategy = org.gradle.api.file.DuplicatesStrategy.EXCLUDE
        from(project.projectDir) {
            exclude(exclusions)
        }
    }
}

// Task to create a ZIP archive
tasks.register<Zip>("createSrcZipArchive") {
    configureArchiveTask(this)
}

// Task to create a ZIP archive of AAR files
tasks.register<Zip>("createLibZipArchive") {
    //dependsOn("publishAllPublicationsToLocalDist")
    configureArchiveTask(this)
    // Use the project's outputs.
    from(layout.buildDirectory.dir("localDist")) {
        include("**/*.jar")
        include("**/*.klib")
        include("**/*.module")
        include("**/*.pom")
        include("**/*kotlin-tooling-metadata.json")
    }
}


// Task to create a tar.gz archive
tasks.register<Tar>("createTarGzArchive") {
    configureArchiveTask(this)
    compression = org.gradle.api.tasks.bundling.Compression.GZIP
}

// Ensure tasks are run in the proper order if other tasks might conflict with them
tasks.named("createSrcZipArchive") {
    mustRunAfter("createTarGzArchive")
}
//tasks.named("createTarGzArchive") {
//mustRunAfter("createSrcZipArchive")
//}
