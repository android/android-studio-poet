package com.google.androidstudiopoet.generators.android_modules

import com.google.androidstudiopoet.models.*
import com.google.androidstudiopoet.testutils.mock
import com.google.androidstudiopoet.writers.FileWriter
import com.nhaarman.mockitokotlin2.*
import org.junit.Test

class AndroidModuleBuildBazelGeneratorTest {
    private val fileWriter: FileWriter = mock()
    private val androidModuleBuildBazelGenerator = AndroidModuleBuildBazelGenerator(fileWriter)

    @Test
    fun `generator sets correct android_binary rule class for Application module`() {
        val blueprint = getAndroidBuildBazelBlueprint(
            isApplication = true,
            dependencies = setOf(
                ModuleDependency("library1", mock(), "unused"),
                GmavenBazelDependency("external:aar:1")
        ))
        androidModuleBuildBazelGenerator.generate(blueprint)
        val expected = """load("@gmaven_rules//:defs.bzl", "gmaven_artifact")

android_binary(
    name = "example",
    srcs = glob(["src/main/java/**/*.java"]),
    multidex = "native",
    resource_files = glob(["src/main/res/**/*"]),
    manifest = "src/main/AndroidManifest.xml",
    custom_package = "com.example",
    visibility = ["//visibility:public"],
    deps = [
        "//library1",
        gmaven_artifact("external:aar:1")
    ],
)"""
        verify(fileWriter).writeToFile(expected, "BUILD.bazel")
    }

    @Test
    fun `generator applies libraries from the blueprint`() {
        val blueprint = getAndroidBuildBazelBlueprint(dependencies = setOf(
            ModuleDependency("library1", mock(), "unused"),
            ModuleDependency("library2", mock(), "unused"),
            GmavenBazelDependency("external:aar:1"),
            GmavenBazelDependency("external:aar:2")
        ))
        androidModuleBuildBazelGenerator.generate(blueprint)
        val expected = """load("@gmaven_rules//:defs.bzl", "gmaven_artifact")

android_library(
    name = "example",
    srcs = glob(["src/main/java/**/*.java"]),
    resource_files = glob(["src/main/res/**/*"]),
    manifest = "src/main/AndroidManifest.xml",
    custom_package = "com.example",
    visibility = ["//visibility:public"],
    deps = [
        "//library1",
        "//library2",
        gmaven_artifact("external:aar:1"),
        gmaven_artifact("external:aar:2")
    ],
)"""
        verify(fileWriter).writeToFile(expected, "BUILD.bazel")
    }

    @Test
    fun `generator sets correct target name from the blueprint`() {
        val blueprint = getAndroidBuildBazelBlueprint(packageName = "com.foo", targetName = "foo")
        androidModuleBuildBazelGenerator.generate(blueprint)
        val expected = """load("@gmaven_rules//:defs.bzl", "gmaven_artifact")

android_library(
    name = "foo",
    srcs = glob(["src/main/java/**/*.java"]),
    resource_files = glob(["src/main/res/**/*"]),
    manifest = "src/main/AndroidManifest.xml",
    custom_package = "com.foo",
    visibility = ["//visibility:public"],
)"""
        verify(fileWriter).writeToFile(expected, "BUILD.bazel")
    }

    private fun getAndroidBuildBazelBlueprint(
            isApplication: Boolean = false,
            packageName: String = "com.example",
            targetName: String = "example",
            dependencies: Set<Dependency> = setOf()
    ): AndroidBuildBazelBlueprint {
        val blueprint = mock<AndroidBuildBazelBlueprint>()
        whenever(blueprint.isApplication).thenReturn(isApplication)
        whenever(blueprint.packageName).thenReturn(packageName)
        whenever(blueprint.dependencies).thenReturn(dependencies)
        whenever(blueprint.path).thenReturn("BUILD.bazel")
        whenever(blueprint.name).thenReturn(targetName)
        return blueprint
    }
}