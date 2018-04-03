/*
Copyright 2017 Google Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.google.androidstudiopoet.gradle

interface Statement {
    fun toGroovy(indentNumber: Int): String
}

data class StringStatement(val value: String) : Statement {
    override fun toGroovy(indentNumber: Int): String = INDENT.repeat(indentNumber) + value
}

data class Expression(val left: String, val right: String) : Statement {
    override fun toGroovy(indentNumber: Int): String = "${INDENT.repeat(indentNumber)}$left $right"
}

class Closure(val name: String, val statements: List<Statement>) : Statement {
    override fun toGroovy(indentNumber: Int): String {
        val indent = INDENT.repeat(indentNumber)
        return """$indent$name {
${statements.joinToString(separator = "\n") { it.toGroovy(indentNumber + 1) }}
$indent}"""
    }
}

data class Task(val name: String, val arguments: List<TaskParameter>, val statements: List<Statement>) : Statement {
    override fun toGroovy(indentNumber: Int): String {
        val indent = INDENT.repeat(indentNumber)
        return """${indent}task $name(${arguments.joinToString { "${it.name}: ${it.value}" }}) {
${statements.joinToString(separator = "\n") { it.toGroovy(indentNumber + 1) }}
$indent}"""
    }

}

data class TaskParameter(val name: String, val value: String)

private const val INDENT = "    "