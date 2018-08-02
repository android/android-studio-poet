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

import com.google.androidstudiopoet.utils.joinPath

class LayoutBlueprint(val name: String, layoutsDir: String,
                      stringsWithDataBindingListenersToUse: List<Pair<String, ClassBlueprint?>>,
                      imagesWithDataBindingListenersToUse: List<Pair<String, ClassBlueprint?>>,
                      val layoutsToInclude: List<String>) {

    val filePath = layoutsDir.joinPath(name) + ".xml"
    val textViewsBlueprints = stringsWithDataBindingListenersToUse.mapIndexed { index, it ->
        TextViewBlueprint("$name${it.first}$index", it.first, it.second?.toOnClickAction())
    }

    val imageViewsBlueprints = imagesWithDataBindingListenersToUse.mapIndexed { index, it ->
        ImageViewBlueprint("$name${it.first}$index", it.first, it.second?.toOnClickAction())
    }

    val classesToBind
            = (stringsWithDataBindingListenersToUse + imagesWithDataBindingListenersToUse).mapNotNull { it.second }

    val hasLayoutTag = classesToBind.isNotEmpty()
}


fun ClassBlueprint.toOnClickAction(): String {
    return "(view) -> ${className.decapitalize()}.${getMethodToCallFromOutside()!!.methodName}()"
}