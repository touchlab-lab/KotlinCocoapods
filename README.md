# Kotlin Xcode Sync

*Note* Soon to be deprecated. You can add folder references instead. [See here](https://github.com/touchlab/xcode-kotlin/issues/16).

Import kotlin files into an Xcode project. This is used in conjunction with the [Xcode 
Kotlin plugin](https://github.com/touchlab/xcode-kotlin) to allow for Kotlin/Native debugging in an iOS application.

Importing Kotlin files into Xcode is somewhat tricky to do manually. This plugin will facilitate
that.

It is called "Sync", but currently it will only add new files. Renamed or removed files will
need to be handled manually in Xcode.

> ## **We're Hiring!**
>
> Touchlab is looking for Android-focused mobile engineers, experienced with Kotlin and 
> looking to get involved with Kotlin Multiplatorm in the near future. [More info here](https://on.touchlab.co/2NrAhB8).

## Usage

Add the following to the buildscript section:

```groovy
buildscript {
    dependencies {
        classpath 'co.touchlab:kotlinxcodesync:0.2'
    }
}
```

Apply the plugin in the shared code project, and configure the plugin

```groovy
apply plugin: 'co.touchlab.kotlinxcodesync'


xcodeSync {
  projectPath = "../../iosApp/iosApp.xcodeproj"
  target = "iosApp"
}
```

The 'projectPath' points at the Xcode project folder. 'target' is the target inside the Xcode project. There's also the optional 
parameter 'group', which by default is set to 'Kotlin'. That is the group folder that files are copied into.
