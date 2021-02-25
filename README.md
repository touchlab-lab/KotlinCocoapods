[![View on Maven Central](https://img.shields.io/maven-central/v/co.touchlab/kotlinnativecocoapods)](https://search.maven.org/artifact/co.touchlab/kotlinnativecocoapods)

# Kotlin Cocoapods Extended

This plugin is a fork of the Kotlin Multiplatform plugin's cocoapods support. Cocoapods support bundled into the KMP 
Gradle plugin is somewhat limited, and trying to change how that works is difficult because it is bundled with the main
plugin and the main Kotlin project itself. This fork has pulled out just the Cocoapods support and changed the namespace
so it won't clash.

## Features

The main Cocoapods plugin configures the Xcode framework that is built, and does not support all of the configuration 
options available when you configure the framework manually. This means you'll get a static framework, which can't be 
debugged, and will prevent being able to add multiple frameworks to a single project. There has also been difficulty
configuring dependencies to be included in the generated header.

Not all of this is supported now, but by forking and removing the Cocoapods plugin, it will be much easier to add features
as desired.

## Usage

Add the following to the buildscript section:

```kotlin
buildscript {
    dependencies {
        classpath("co.touchlab:kotlinnativecocoapods:0.12")
    }
}
```

Apply the plugin in the shared code project, and configure the plugin

```kotlin
plugins {
    id("co.touchlab.native.cocoapods")
}

kotlin {
    cocoapodsext {
        summary = "Common library for the KaMP starter kit"
        homepage = "https://github.com/touchlab/KaMPStarter"
        framework {
            isStatic = false
            export("com.example:dependency:1.0)
            transitiveExport = true
        }
    }
}
```

## Primary Maintainer

[Russell Wolf](https://github.com/russhwolf/)

![](https://avatars.githubusercontent.com/u/3256243?s=140&v=4)

*Reach out on [Twitter](twitter.com/russhwolf) or [Kotlinlang Slack](https://kotlinlang.slack.com/archives/D2VU3UHU0)*
