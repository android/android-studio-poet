package com.google.androidstudiopoet.models

import java.io.File

data class PackageBlueprint(val packageIndex: Int, val moduleIndex: Int, val classesPerPackage: Int,
                            val methodsPerClass: Int, val mainPackage: String, val moduleRoot: File)
