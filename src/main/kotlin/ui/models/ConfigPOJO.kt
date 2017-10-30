/*
 *  Copyright 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package ui.models

import com.google.gson.Gson

class ConfigPOJO {

    // directory where generator should put all generated packages
    var root: String? = null

    // how many modules
    var numModules: Int = 0

    // how many methods should be generated all together (!!!)
    var allMethods: String? = null

    // how many java methods should be generated all together
    var javaMethodCount: String? = null

    // how many java packages should be generated
    var javaPackageCount: String? = null

    // how many kotlin packages should be generated
    var kotlinPackageCount: String? = null

    // how many classes should be generated in each Java package
    var javaClassCount: String? = null

    // how many classes should be generated in each Kotlin package
    var kotlinClassCount: String? = null

    val javaMethodsPerClass: Int
        get() = Integer.parseInt(javaMethodCount!!) / (Integer.parseInt(javaClassCount!!) * Integer.parseInt(javaPackageCount!!))

    private val allKotlinMethods: Int
        get() = Integer.parseInt(allMethods!!) - Integer.parseInt(javaMethodCount!!)

    val kotlinMethodsPerClass: Int
        get() = allKotlinMethods / (Integer.parseInt(kotlinClassCount!!) * Integer.parseInt(kotlinPackageCount!!))

    var dependencies: List<Dependency>? = null

    override fun toString(): String = toJson()

    private fun toJson(): String {
        val gson = Gson()

        return gson.toJson(this)
    }
}

