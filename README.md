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

See [Releases](https://github.com/touchlab/KotlinCocoapods/releases) for current version.

Add the following to the buildscript section:

```groovy
buildscript {
    dependencies {
        classpath 'co.touchlab:kotlinnativecocoapods:0.x'
    }
}
```

Apply the plugin in the shared code project, and configure the plugin

```groovy
plugins {
  id("co.touchlab.kotlinxcodesync")
}

kotlin {
    cocoapodsext {
      summary = "Common library for the KaMP starter kit"
      homepage = "https://github.com/touchlab/KaMPStarter"
      isStatic = false
    }
}
```