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
import com.google.androidstudiopoet.models.LayoutBlueprint
import com.google.androidstudiopoet.writers.FileWriter
import com.squareup.kotlinpoet.*


class KotlinActivityGenerator(var fileWriter: FileWriter): ActivityGenerator {

    override fun generate(blueprint: ActivityBlueprint) {

        val activityClassBuilder = blueprint.toClazzSpec()
        blueprint.fields.forEach {
            activityClassBuilder.addProperty(it.toPropertySpec())
        }
        activityClassBuilder.addFunction(blueprint.toOnCreateFunSpec())
        val fileSpec = FileSpec.builder(blueprint.packageName, blueprint.className)
                .addType(activityClassBuilder.build())
        blueprint.toComposeFunc()?.let(fileSpec::addFunction)
        val content = buildString {
            fileSpec.build().writeTo(this)
        }
        fileWriter.writeToFile(content, "${blueprint.where}/${blueprint.className}.kt")
    }

    private fun ActivityBlueprint.toClazzSpec(): TypeSpec.Builder {
        val superClass = if (enableCompose) {
            ClassName("androidx.activity", "ComponentActivity")
        } else {
            ClassName("android.app", "Activity")
        }
        return TypeSpec.classBuilder(className)
                .superclass(superClass)
    }

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

    private fun ActivityBlueprint.toComposeFunc(): FunSpec? {
        if (enableCompose) {
            val text = MemberName("androidx.compose.material", "Text")
            val column = MemberName("androidx.compose.foundation.layout", "Column")
            val image = MemberName("androidx.compose.foundation", "Image")
            val clickable = MemberName("androidx.compose.foundation", "clickable")
            val rememberScrollableState = MemberName("androidx.compose.foundation", "rememberScrollState")
            val verticalScroll = MemberName("androidx.compose.foundation", "verticalScroll")
            val modifier = ClassName("androidx.compose.ui", "Modifier")
            val painterResource = MemberName("androidx.compose.ui.res", "painterResource")
            val stringResource = MemberName("androidx.compose.ui.res", "stringResource")
            val builder = FunSpec.builder("Screen")
                    .addAnnotation(ClassName("androidx.compose.runtime", "Composable"))
                    .addModifiers(KModifier.PRIVATE)
                    .addStatement("val scrollState = %M()", rememberScrollableState)
                    .beginControlFlow("%M(modifier = %T.%M(scrollState))", column, modifier, verticalScroll)
            layout.textViewsBlueprints.forEach { textViewBlueprint ->
                if (textViewBlueprint.actionClass != null) {
                    builder.addStatement("%M(text = %M(R.string.%L), modifier = %T.%M { %T().%L() })",
                            text,
                            stringResource,
                            textViewBlueprint.stringName,
                            modifier,
                            clickable,
                            ClassName.bestGuess(textViewBlueprint.actionClass.fullClassName),
                            textViewBlueprint.actionClass.getMethodToCallFromOutside()!!.methodName

                    )
                } else {
                    builder.addStatement("%M(text = %M(R.string.%L))", text, stringResource, textViewBlueprint.stringName)
                }
            }
            layout.imageViewsBlueprints.forEach { imageViewBlueprint ->
                if (imageViewBlueprint.actionClass != null) {
                    builder.addStatement("%M(painter = %M(R.drawable.%L), contentDescription = null, modifier = %T.%M { %T().%L() })",
                            image,
                            painterResource,
                            imageViewBlueprint.imageName,
                            modifier,
                            clickable,
                            ClassName.bestGuess(imageViewBlueprint.actionClass.fullClassName),
                            imageViewBlueprint.actionClass.getMethodToCallFromOutside()!!.methodName
                    )
                } else {
                    builder.addStatement("%M(painter = %M(R.drawable.%L), contentDescription = null)",
                            image,
                            painterResource,
                            imageViewBlueprint.imageName
                    )
                }
            }
            builder.endControlFlow()
            return builder.build()
        } else {
            return null
        }
    }

    private fun ActivityBlueprint.toOnCreateFunSpec(): FunSpec {
        val builder = FunSpec.builder("onCreate")
                .addModifiers(KModifier.OVERRIDE)
                .addParameter("savedInstanceState", ClassName("android.os", "Bundle").copy(nullable = true))
                .addStatement("super.onCreate(savedInstanceState)")
        classToReferFromActivity.getMethodToCallFromOutside()?.let {
            val className = ClassName.bestGuess(it.className)
            builder.addStatement("%T().%N()", className, it.methodName)
        }
        if (enableCompose) {
            val setContent = MemberName("androidx.activity.compose", "setContent")
            builder.beginControlFlow("%M", setContent)
            builder.addStatement("Screen()")
            builder.endControlFlow()
        } else if (enableDataBinding) {
            val bindingClassName = ClassName.bestGuess(dataBindingClassName)
            val dataBindingUtil = ClassName("androidx.databinding", "DataBindingUtil")
            builder.addStatement("val binding: %T = %T.setContentView(this, R.layout.${layout.name})", bindingClassName, dataBindingUtil)
            listenerClassesForDataBinding.forEach {
                builder.addStatement("binding.%N = %T()", it.className.decapitalize(), ClassName.bestGuess(it.fullClassName))
            }
        } else if (enableViewBinding) {
            val dataBindingClass = ClassName("$packageName.databinding", layout.findViewBindingClassName())
            builder.addStatement("val binding = %T.inflate(layoutInflater)", dataBindingClass)
            builder.addStatement("setContentView(binding.root)")
        } else {
            builder.addStatement("setContentView(R.layout.${layout.name})")
        }
        return builder.build()
    }
}

private fun LayoutBlueprint.findViewBindingClassName(): String =
        name.split("_").joinToString(separator = "") { it.capitalize() } + "Binding"
