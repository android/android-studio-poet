package com.google.androidstudiopoet.generators

import com.google.androidstudiopoet.models.BazelWorkspaceBlueprint
import com.google.androidstudiopoet.testutils.mock
import com.google.androidstudiopoet.utils.joinPath
import com.google.androidstudiopoet.writers.FileWriter
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Test

class BazelWorkspaceGeneratorTest {

  private val fileWriter: FileWriter = mock()
  private val bazelWorkspaceGenerator= BazelWorkspaceGenerator(fileWriter)

  @Test
  fun `generator generates workspace file with correct base content`() {
    val blueprint = getBazelWorkspaceBlueprint()
    bazelWorkspaceGenerator.generate(blueprint)
    val expected = """android_sdk_repository(
    name = "androidsdk",
)

# Google Maven Repository
load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")
GMAVEN_TAG = "19700101-1"
http_archive(
    name = "gmaven_rules",
    strip_prefix = "gmaven_rules-%s" % GMAVEN_TAG,
    urls = ["https://github.com/bazelbuild/gmaven_rules/archive/%s.tar.gz" % GMAVEN_TAG],
)
load("@gmaven_rules//:gmaven.bzl", "gmaven_rules")
gmaven_rules()
"""
    verify(fileWriter).writeToFile(expected, "WORKSPACE")
  }

  @Test
  fun `generator generates workspace file with correct gmaven rules tag`() {
    val blueprint = getBazelWorkspaceBlueprint("20001212-42")
    bazelWorkspaceGenerator.generate(blueprint)
    val expected = """android_sdk_repository(
    name = "androidsdk",
)

# Google Maven Repository
load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")
GMAVEN_TAG = "20001212-42"
http_archive(
    name = "gmaven_rules",
    strip_prefix = "gmaven_rules-%s" % GMAVEN_TAG,
    urls = ["https://github.com/bazelbuild/gmaven_rules/archive/%s.tar.gz" % GMAVEN_TAG],
)
load("@gmaven_rules//:gmaven.bzl", "gmaven_rules")
gmaven_rules()
"""
    verify(fileWriter).writeToFile(expected, "WORKSPACE")
  }

  private fun getBazelWorkspaceBlueprint(gmavenRulesTag: String = "19700101-1"): BazelWorkspaceBlueprint {
    val blueprint = mock<BazelWorkspaceBlueprint>()
    whenever(blueprint.workspacePath).thenReturn("WORKSPACE")
    whenever(blueprint.gmavenRulesTag).thenReturn(gmavenRulesTag)
    return blueprint
  }

}