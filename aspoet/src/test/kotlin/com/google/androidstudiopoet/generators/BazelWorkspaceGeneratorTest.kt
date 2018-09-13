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
  fun `generator generates workspace file with bazelWorkspaceContent`() {
    val blueprint = getBazelWorkspaceBlueprint()
    bazelWorkspaceGenerator.generate(blueprint)
    val expected = "android_sdk_repository(name = \"androidsdk\")"
    verify(fileWriter).writeToFile(expected, "WORKSPACE")
  }

  private fun getBazelWorkspaceBlueprint(): BazelWorkspaceBlueprint {
    val blueprint = mock<BazelWorkspaceBlueprint>()
    whenever(blueprint.workspacePath).thenReturn("WORKSPACE")
    return blueprint
  }

}