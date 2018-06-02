package com.google.androidstudiopoet.models

import com.google.androidstudiopoet.utils.joinPath

class GradlePropertiesBlueprint(projectRoot: String, val properties: Map<String, String>?) {
    val path = projectRoot.joinPath("gradle.properties")
}