package com.google.androidstudiopoet.models

interface ModuleBuildSpecificationBlueprint {

    val plugins: Set<String>
    val path: String
    val dependencies: Set<Dependency>
    val extraLines: List<String>?

}