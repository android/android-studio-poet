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

package com.google.androidstudiopoet.models

abstract class ClassBlueprint(val packageName: String, val className: String) {
    val fullClassName = "$packageName.$className"
    abstract fun getFieldBlueprints(): List<FieldBlueprint>
    abstract fun getMethodBlueprints(): List<MethodBlueprint>
    abstract fun getClassPath(): String
    abstract fun getMethodToCallFromOutside(): MethodToCall?
}

internal fun ClassBlueprint.toDataBindingOnClickAction(): String =
    "(view) -> ${className.decapitalize()}.${getMethodToCallFromOutside()!!.methodName}()"
