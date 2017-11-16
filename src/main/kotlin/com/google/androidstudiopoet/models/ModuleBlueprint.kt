package com.google.androidstudiopoet.models

data class ModuleBlueprint(val index: Int, val name: String, val root: String, val dependencies: List<String>,
                           val methodsToCall: List<MethodToCall>)