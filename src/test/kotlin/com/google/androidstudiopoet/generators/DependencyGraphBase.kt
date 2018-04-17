/*
Copyright 2018 Google Inc.

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

import com.google.androidstudiopoet.models.*
import com.google.androidstudiopoet.testutils.mock
import com.nhaarman.mockito_kotlin.whenever

abstract class DependencyGraphBase {
    private val testDependencies = mapOf(
            "app0" to listOf(
                    AndroidModuleDependency("android0", MethodToCall("foo0", "class0"), "api", ResourcesToRefer(emptyList(), emptyList(), emptyList())),
                    AndroidModuleDependency("android1", MethodToCall("foo1", "class1"), "implementation", ResourcesToRefer(emptyList(), emptyList(), emptyList())),
                    ModuleDependency("module0", MethodToCall("foo0", "class0"), "runtimeOnly" ),
                    ModuleDependency("module1", MethodToCall("foo1", "class1"), "testImplementation" ),
                    ModuleDependency("module2", MethodToCall("foo2", "class2"), "testRuntimeOnly" ),
                    ModuleDependency("module3", MethodToCall("foo3", "class3"), "apiElements" )
            ),
            "android0" to listOf(
                    AndroidModuleDependency("android1", MethodToCall("foo1", "class1"), "runtimeElements", ResourcesToRefer(emptyList(), emptyList(), emptyList())),
                    ModuleDependency("module0", MethodToCall("foo0", "class0"), "compileClassPath" ),
                    ModuleDependency("module1", MethodToCall("foo1", "class1"), "runtimeClassPath" ),
                    ModuleDependency("module2", MethodToCall("foo2", "class2"), "testCompileClassPath" ),
                    ModuleDependency("module3", MethodToCall("foo3", "class3"), "testRuntimeClassPath" )
            ),
            "android1" to listOf(
                    ModuleDependency("module0", MethodToCall("foo0", "class0"), "compileOnly" ),
                    ModuleDependency("module1", MethodToCall("foo1", "class1"), "testCompileOnly" ),
                    ModuleDependency("module2", MethodToCall("foo2", "class2"), "unknown" ),
                    ModuleDependency("module3", MethodToCall("foo3", "class3"), "api" ),
                    ModuleDependency("module3", MethodToCall("foo3", "class3"), "implementation" )
            ),
            "module0" to listOf(
                    ModuleDependency("module1", MethodToCall("foo1", "class1"), "api" ),
                    ModuleDependency("module2", MethodToCall("foo2", "class2"), "implementation" )
            ),
            "module1" to listOf(
                    ModuleDependency("module2", MethodToCall("foo2", "class2"), "implementation" )
            ),
            "module3" to listOf(),
            "module4" to listOf()
    )
    private val testModuleBlueprints = listOf(
            getAndroidModuleBlueprint("app0", hasLaunchActivity = true),
            getAndroidModuleBlueprint("android0"),
            getAndroidModuleBlueprint("android1"),
            getModuleBlueprint("module0"),
            getModuleBlueprint("module1"),
            getModuleBlueprint("module2"),
            getModuleBlueprint("module3"),
            getModuleBlueprint("module4")
    )

    val expectedGraphText = """digraph projectName {
  app0 -> android0, android1, module0, module1, module2, module3;
  android0 -> android1, module0, module1, module2, module3;
  android1 -> module0, module1, module2, module3, module3;
  module0 -> module1, module2;
  module1 -> module2;
  module2;
  module3;
  module4;
}"""

    fun getProjectBlueprint(): ProjectBlueprint {
        return mock<ProjectBlueprint>().apply {
            whenever(this.projectRoot).thenReturn("projectRoot")
            whenever(this.projectName).thenReturn("projectName")
            whenever(this.allDependencies).thenReturn(testDependencies)
            whenever(this.allModuleBlueprints).thenReturn(testModuleBlueprints)
        }
    }

    private fun getModuleBlueprint(name: String): ModuleBlueprint {
        return mock<ModuleBlueprint>().apply {
            whenever(this.name).thenReturn(name)
        }
    }

    private fun getAndroidModuleBlueprint(name: String, hasLaunchActivity: Boolean = false): AndroidModuleBlueprint {
        return mock<AndroidModuleBlueprint>().apply {
            whenever(this.name).thenReturn(name)
            whenever(this.hasLaunchActivity).thenReturn(hasLaunchActivity)
        }
    }
}