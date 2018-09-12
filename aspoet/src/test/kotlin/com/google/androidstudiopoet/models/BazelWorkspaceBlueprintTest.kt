package com.google.androidstudiopoet.models

import com.google.androidstudiopoet.testutils.assertEquals
import com.google.androidstudiopoet.utils.joinPath
import org.junit.Test

class BazelWorkspaceBlueprintTest {

  @Test
  fun `workspace path is at the root of the project`() {
    val blueprint = getBazelWorkspaceBlueprint("rootPath")
    blueprint.workspacePath.assertEquals("rootPath".joinPath("WORKSPACE"))
  }

  @Test
  fun `workspace file content contains android_sdk_repository declaration`() {
    val blueprint = getBazelWorkspaceBlueprint()
    val androidSdkRepositoryDeclaration = """android_sdk_repository(
    name = "androidsdk"
)"""
    assert(blueprint.bazelWorkspaceContent.contains(androidSdkRepositoryDeclaration))
  }

  private fun getBazelWorkspaceBlueprint(
      projectRoot: String = "foo"
  ) = BazelWorkspaceBlueprint(projectRoot)

}

