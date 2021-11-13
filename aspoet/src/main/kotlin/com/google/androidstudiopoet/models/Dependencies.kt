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

open class ModuleDependency(val name: String, val methodToCall: MethodToCall, val method: String) : Dependency

class AndroidModuleDependency(name: String, methodToCall: MethodToCall, method: String, val resourcesToRefer: ResourcesToRefer)
    : ModuleDependency(name, methodToCall, method)

data class LibraryDependency(val method: String, val name: String) : Dependency

data class FileTreeDependency(val method: String, val dir: String, val include: String, val count: Int) : Dependency

data class GmavenBazelDependency(val name: String) : Dependency

interface Dependency
