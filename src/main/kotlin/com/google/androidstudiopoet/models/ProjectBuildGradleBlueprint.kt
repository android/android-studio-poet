package com.google.androidstudiopoet.models

class ProjectBuildGradleBlueprint(val root: String, enableKotlin: Boolean, androidGradlePluginVersion: String) {

    val classpaths by lazy {
        listOfNotNull(
                "com.android.tools.build:gradle:$androidGradlePluginVersion",
                if (enableKotlin) "org.jetbrains.kotlin:kotlin-gradle-plugin:${'$'}kotlin_version" else null
        ).toSet()
    }

    val repositories = setOf(
            Repository.Named("jcenter"),
            Repository.Named("google")
    )
}
