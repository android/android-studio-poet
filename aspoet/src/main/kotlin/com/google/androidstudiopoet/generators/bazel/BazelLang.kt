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

package com.google.androidstudiopoet.generators.bazel

/**
 * Bazel uses the Starlark configuration language for BUILD files and .bzl extensions.
 *
 * https://docs.bazel.build/versions/master/skylark/language.html
 */

private const val BASE_INDENT = "    "

interface Statement

/**
 * foo = "bar"
 */
data class AssignmentStatement(val lhs: String, val rhs: String) : Statement {
    override fun toString() = """$lhs = $rhs"""
}

/**
 * load("@foo//bar:baz.bzl", "symbol_foo", "symbol_bar")
 */
data class LoadStatement(val module: String, val symbols: List<String>) : Statement {
    override fun toString() = """load("$module", ${symbols.map { "\"$it\"" }.joinToString(separator = ", ")})"""
}

/**
 * # Comment foo bar baz
 */
data class Comment(val comment: String) {
    override fun toString(): String =  """# $comment"""
}

/**
 * A BUILD / BUILD.bazel target.
 *
 * A target is made of
 *
 * 1) a rule class.
 * 2) mandatory "name" attribute.
 * 3) other rule-specific attributes.
 *
 * e.g.
 *
 * android_library(
 *     name = "lib_foo",
 *     srcs = glob(["src/**/*.java"]),
 *     deps = [":lib_bar"],
 *     manifest = "AndroidManifest.xml",
 *     custom_package = "com.example.foo",
 *     resource_files = glob(["res/**/*"]),
 * )
 */
data class Target(val ruleClass: String, val attributes: List<Attribute>) : Statement {
    override fun toString() : String {
        return when (attributes.size) {
            0 -> "$ruleClass()"
            else -> """$ruleClass(
$BASE_INDENT${attributes.joinToString(separator = ",\n$BASE_INDENT") { it.toString() }}
)"""
        }
    }
}

/**
 * Data classes representing attributes in targets.
 *
 * To improve type safety, create data classes implementing Attribute, e.g. StringAttribute.
 *
 * Bazel supports the following attribute types:
 *
 * bool
 * int
 * int_list
 * label
 * label_keyed_string_dict
 * label_list
 * license
 * output
 * output_list
 * string
 * string_dict
 * string_list
 * string_list_dict
 *
 * List of attribute schemas from:
 * https://docs.bazel.build/versions/master/skylark/lib/attr.html
 */

interface Attribute {
    val name: String
    val value: Any
}

data class RawAttribute(override val name: String, override val value: String): Attribute {
    override fun toString() = """$name = $value"""
}

data class StringAttribute(override val name: String, override val value: String): Attribute {
    override fun toString() = "$name = \"$value\""
}
