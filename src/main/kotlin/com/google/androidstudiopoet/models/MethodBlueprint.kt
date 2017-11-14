package com.google.androidstudiopoet.models

data class MethodBlueprint(private val methodNumber: Int, val statements: List<String?>) {
    val methodName = "foo" + methodNumber
}


