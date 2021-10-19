/*
Copyright 2021 Google Inc.

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
import com.google.androidstudiopoet.models.AnnotationBlueprint
import com.google.androidstudiopoet.models.FieldBlueprint
import com.google.androidstudiopoet.writers.FileWriter
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.MemberName.Companion.member


class KotlinActivityGenerator(var fileWriter: FileWriter): ActivityGenerator {

    override fun generate(blueprint: ActivityBlueprint) {

        val activityClassBuilder = getClazzSpec(blueprint.className)
        blueprint.fields.forEach {
            activityClassBuilder.addProperty(it.toPropertySpec())
        }
        activityClassBuilder.addFunction(blueprint.toOnCreateFunSpec())
        val content = buildString {
            FileSpec.builder(blueprint.packageName, blueprint.className)
                    .addType(activityClassBuilder.build())
                    .build()
                    .writeTo(this)
        }
        fileWriter.writeToFile(content, "${blueprint.where}/${blueprint.className}.kt")
    }

    private fun getClazzSpec(activityClassName: String) =
            TypeSpec.classBuilder(activityClassName)
                    .superclass(ClassName("android.app", "Activity"))

    private fun FieldBlueprint.toPropertySpec(): PropertySpec {
        val propertySpec = PropertySpec.builder(name, ClassName.bestGuess(typeName))
                .mutable()
                .addModifiers(KModifier.LATEINIT)
        annotations.forEach {
            propertySpec.addAnnotation(it.toAnnotationSpec())
        }
        return propertySpec.build()
    }

    private fun AnnotationBlueprint.toAnnotationSpec(): AnnotationSpec {
        val builder = AnnotationSpec.builder(ClassName.bestGuess(className))
        params.forEach { (key, value) ->
            builder.addMember("$key = %S", value)
        }
        return builder.build()
    }

    private fun ActivityBlueprint.toOnCreateFunSpec(): FunSpec {
        val builder = FunSpec.builder("onCreate")
                .addModifiers(KModifier.OVERRIDE)
                .returns(Unit::class)
                .addParameter(
                        "savedInstanceState",
                        ClassName("android.os", "Bundle").copy(nullable = true)
                )
                .addStatement("super.onCreate(savedInstanceState)")
        classToReferFromActivity.getMethodToCallFromOutside()?.let {
            val className = ClassName.bestGuess(it.className)
            builder.addStatement("%T().%N()", className, className.member(it.methodName))
        }
        if (hasDataBinding) {
            val bindingClassName = ClassName.bestGuess(dataBindingClassName)
            val dataBindingUtil = ClassName("androidx.databinding", "DataBindingUtil")
            builder.addStatement("val binding: %T = %T.setContentView(this, R.layout.${layout.name})", bindingClassName, dataBindingUtil)
            listenerClassesForDataBinding.forEach {
                builder.addStatement("binding.%N = %T()", it.className.decapitalize(), ClassName.bestGuess(it.fullClassName))
            }
        } else {
            builder.addStatement("setContentView(R.layout.${layout.name})")
        }
        return builder.build()
    }
}
