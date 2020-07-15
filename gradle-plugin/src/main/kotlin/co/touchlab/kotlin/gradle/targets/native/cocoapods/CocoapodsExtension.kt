/*
 * Copyright 2010-2019 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

@file:Suppress("PackageDirectoryMismatch") // Old package for compatibility
package co.touchlab.kotlin.gradle.plugin.cocoapods

import org.gradle.api.Action
import org.gradle.api.Named
import org.gradle.api.NamedDomainObjectSet
import org.gradle.api.Project
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.Optional
import org.jetbrains.kotlin.gradle.plugin.mpp.Framework

open class CocoapodsExtension(private val project: Project) {
    @get:Input
    val version: String
        get() = project.version.toString()

    /**
     * Configure authors of the pod built from this project.
     */
    @Optional
    @Input
    var authors: String? = null

    /**
     * Configure license of the pod built from this project.
     */
    @Optional
    @Input
    var license: String? = null

    /**
     * Configure description of the pod built from this project.
     */
    @Optional
    @Input
    var summary: String? = null

    /**
     * Configure homepage of the pod built from this project.
     */
    @Optional
    @Input
    var homepage: String? = null

    private fun Framework.setDefaults() {
        baseName = project.name.asValidFrameworkName()
        isStatic = true
    }

    internal fun configureFramework(frameworkToConfigure: Framework) {
        frameworkToConfigure.setDefaults()
        frameworkAction?.execute(frameworkToConfigure)
    }

    //Not an input because we pull relevant values from the configured frameworks instead of here
    @Internal
    var frameworkAction: Action<Framework>? = null

    fun framework(action: Action<Framework>) {
        frameworkAction = action
    }

    private val _pods = project.container(CocoapodsDependency::class.java)

    // For some reason Gradle doesn't consume the @Nested annotation on NamedDomainObjectContainer.
    @get:Nested
    protected val podsAsTaskInput: List<CocoapodsDependency>
        get() = _pods.toList()

    /**
     * Returns a list of pod dependencies.
     */
    // Already taken into account as a task input in the [podsAsTaskInput] property.
    @get:Internal
    val pods: NamedDomainObjectSet<CocoapodsDependency>
        get() = _pods

    /**
     * Add a CocoaPods dependency to the pod built from this project.
     */
    @JvmOverloads
    fun pod(name: String, version: String? = null, moduleName: String = name.split("/")[0], linkOnly: Boolean = false) {
        check(_pods.findByName(name) == null) { "Project already has a CocoaPods dependency with name $name" }
        _pods.add(CocoapodsDependency(name, version, moduleName, linkOnly))
    }

    data class CocoapodsDependency(
            private val name: String,
            @get:Optional @get:Input val version: String?,
            @get:Input val moduleName: String,
            @get:Input val linkOnly: Boolean
    ) : Named {
        @Input
        override fun getName(): String = name
    }
}