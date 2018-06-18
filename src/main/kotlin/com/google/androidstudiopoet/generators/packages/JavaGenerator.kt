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

import com.google.androidstudiopoet.models.AnnotationBlueprint
import com.google.androidstudiopoet.models.ClassBlueprint
import com.google.androidstudiopoet.models.FieldBlueprint
import com.google.androidstudiopoet.models.MethodBlueprint
import com.google.androidstudiopoet.models.MethodToCall
import com.google.androidstudiopoet.writers.FileWriter
import com.squareup.javapoet.AnnotationSpec
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.FieldSpec
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec
import javax.lang.model.element.Modifier

class JavaGenerator constructor(fileWriter: FileWriter) : PackageGenerator(fileWriter) {

    override fun generateClass(blueprint: ClassBlueprint) {

        val classBuilder = getClazz(blueprint)

        blueprint.getFieldBlueprints()
                .map { it.toJavaSpec() }
                .forEach { classBuilder.addField(it) }

        blueprint.getMethodBlueprints()
                .map { generateMethod(it) }
                .forEach { classBuilder.addMethod(it) }

        val javaFile = JavaFile.builder(blueprint.packageName, classBuilder.build()).build()

        writeFile(blueprint.getClassPath(), javaFile.toString())
    }

    private fun getClazz(blueprint: ClassBlueprint) =
            TypeSpec.classBuilder(blueprint.className)
                    .addModifiers(Modifier.PUBLIC)

    private fun generateMethod(blueprint: MethodBlueprint): MethodSpec {

        val method = MethodSpec.methodBuilder(blueprint.methodName)
                .addModifiers(Modifier.PUBLIC)
                .returns(Void.TYPE)

        blueprint.annotationBlueprints.forEach { it -> method.addAnnotation(it.toJavaSpec()) }

        blueprint.statements.forEach { statement -> statement?.let { method.addStatement(it) } }

        return method.build()
    }
}

fun AnnotationBlueprint.toJavaSpec(): AnnotationSpec {
    val builder = AnnotationSpec.builder(ClassName.bestGuess(className))
    params.forEach { builder.addMember(it.key, "\$L", it.value) }
    return builder.build()
}

fun FieldBlueprint.toJavaSpec(): FieldSpec {
    val fieldSpecBuilder = FieldSpec.builder(ClassName.bestGuess(typeName), name)
    annotations.forEach { fieldSpecBuilder.addAnnotation(it.toJavaSpec()) }
    return fieldSpecBuilder.build()
}
