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

package com.google.androidstudiopoet.generators.packages

import com.google.androidstudiopoet.models.ClassBlueprint
import com.google.androidstudiopoet.models.MethodBlueprint
import com.google.androidstudiopoet.models.MethodToCall
import com.google.androidstudiopoet.writers.FileWriter

class KotlinGenerator constructor(fileWriter: FileWriter) : PackageGenerator(fileWriter) {

    override fun generateClass(blueprint: ClassBlueprint): MethodToCall? {
        val buff = StringBuilder()

        buff.append("package ${blueprint.packageName};\n\n")

        val annotName = blueprint.className + "Fancy"
        buff.append("annotation class " + annotName + "\n")
        buff.append("@" + annotName + "\n")

        buff.append("class ${blueprint.className} {\n")

        blueprint.getMethodBlueprints()
                .withIndex().map { (if (it.index != 0) "\n" else "") + generateMethod(it.value) }
                .fold(buff) { acc, method -> acc.append(method) }

        buff.append("}")

        writeFile(blueprint.getClassPath(), buff.toString())
        return blueprint.getMethodToCallFromOutside()
    }

    private fun generateMethod(blueprint: MethodBlueprint): String {
        val buff = StringBuilder()
        blueprint.annotations.forEach { annotation -> buff.append("  @${annotation.name}\n") }
        buff.append("  fun ${blueprint.methodName}(){\n")
        blueprint.statements.forEach { statement -> statement?.let { buff.append("    $it\n") } }
        buff.append("  }\n")
        return buff.toString()
    }
}
