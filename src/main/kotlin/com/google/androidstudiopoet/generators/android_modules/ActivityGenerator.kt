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

package com.google.androidstudiopoet.generators.android_modules

import com.google.androidstudiopoet.models.ActivityBlueprint
import com.google.androidstudiopoet.models.ClassBlueprint
import com.google.androidstudiopoet.utils.fold
import com.google.androidstudiopoet.writers.FileWriter
import com.squareup.javapoet.*
import javax.lang.model.element.Modifier


class ActivityGenerator(var fileWriter: FileWriter) {

    fun generate(blueprint: ActivityBlueprint) {

        val onCreateMethodStatements = getOnCreateMethodStatements(blueprint)

        val onCreateMethod = getOnCreateMethod(onCreateMethodStatements)

        val activityClass = getClazzSpec(blueprint.className)
                .addMethod(onCreateMethod)
                .build()

        val javaFile = JavaFile.builder(blueprint.packageName, activityClass).build()
        fileWriter.writeToFile(javaFile.toString(), "${blueprint.where}/${blueprint.className}.java")

    }

    private fun getOnCreateMethodStatements(blueprint: ActivityBlueprint): List<String> {
        val classBlueprint = blueprint.classBlueprint
        val statements = getMethodStatementFromClassToCall(classBlueprint)?.let { mutableListOf(it) } ?: mutableListOf()

        if (blueprint.hasDataBinding) {
            statements.addAll(getDataBindingMethodStatements(blueprint.layout, blueprint.dataBindingClassName, blueprint.listenerClassesForDataBinding))
        } else {
            statements.add("setContentView(R.layout.${blueprint.layout})")
        }
        return statements
    }

    private fun getDataBindingMethodStatements(layout: String, dataBindingClassName: String,
                                               listenerClasses: List<ClassBlueprint>): List<String> {
        return listOf("$dataBindingClassName binding = android.databinding.DataBindingUtil.setContentView(this, R.layout.$layout)") +
        listenerClasses.map { "binding.set${it.className}(new ${it.fullClassName}())" }
    }

    private fun getMethodStatementFromClassToCall(classBlueprint: ClassBlueprint) =
            classBlueprint.getMethodToCallFromOutside()?.let { "new ${it.className}().${it.methodName}()" }

    private fun getClazzSpec(activityClassName: String) =
            TypeSpec.classBuilder(activityClassName)
                    .superclass(TypeVariableName.get("android.app.Activity"))
                    .addModifiers(Modifier.PUBLIC)

    private fun getOnCreateMethod(statements: List<String>): MethodSpec {

        val builder = MethodSpec.methodBuilder("onCreate")
                .addAnnotation(Override::class.java)
                .addModifiers(Modifier.PUBLIC)
                .returns(Void.TYPE)
                .addParameter(TypeVariableName.get("android.os.Bundle"), "savedInstanceState")
                .addStatement("super.onCreate(savedInstanceState)")

        statements.forEach { builder.addStatement(it) }
        return builder.build()
    }

}
