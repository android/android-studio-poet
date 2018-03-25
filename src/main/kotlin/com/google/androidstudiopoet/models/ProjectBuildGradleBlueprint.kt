package com.google.androidstudiopoet.models

class ProjectBuildGradleBlueprint(val root: String) {

    val repositories = setOf(
            Repository.Named("jcenter"),
            Repository.Named("google")
    )
}
