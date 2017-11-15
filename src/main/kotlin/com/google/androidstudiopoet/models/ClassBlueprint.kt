package com.google.androidstudiopoet.models

abstract class ClassBlueprint(val packageName: String, val className: String) {
    val fullClassName = "$packageName.$className"
    abstract fun getMethodBlueprints(): List<MethodBlueprint>
    abstract fun getClassPath(): String
    abstract fun getMethodToCallFromOutside(): MethodToCall
}