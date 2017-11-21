package com.google.androidstudiopoet.models

data class ModuleDependency(val name: String, val methodToCall: MethodToCall) {

    override fun toString():String = name
}

