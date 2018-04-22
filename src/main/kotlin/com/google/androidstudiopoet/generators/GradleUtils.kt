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

package com.google.androidstudiopoet.generators

import com.google.androidstudiopoet.gradle.Closure
import com.google.androidstudiopoet.gradle.Expression
import com.google.androidstudiopoet.gradle.StringStatement
import com.google.androidstudiopoet.models.Dependency
import com.google.androidstudiopoet.models.LibraryDependency
import com.google.androidstudiopoet.models.ModuleDependency
import com.google.androidstudiopoet.models.Repository

fun ModuleDependency.toExpression() = Expression(this.method, "project(':${this.name}')")

fun LibraryDependency.toExpression() = Expression(this.method, "\"${this.name}\"")

fun String.toApplyPluginExpression() = Expression("apply plugin:", "'$this'")

fun String.toClasspathExpression() = Expression("classpath", "\"$this\"")

fun Repository.toExpression() = when (this) {
    is Repository.Named -> this.toExpression()
    is Repository.Remote -> this.toExpression()
}

fun Repository.Named.toExpression() = StringStatement("${this.name}()")

fun Repository.Remote.toExpression() = Closure("maven", listOf(Expression("url", "\"${this.url}\"")))

fun Dependency.toExpression() = when (this) {
    is ModuleDependency -> this.toExpression()
    is LibraryDependency -> this.toExpression()
    else -> null
}
