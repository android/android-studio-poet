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
  fun `blueprint stores GMAVEN_TAG in YYYYMMDD-{snapshot num} form`() {
    val blueprint = getBazelWorkspaceBlueprint()
    assert(blueprint.gmavenRulesTag.contains(Regex("\\d{8}-\\d+")))
  }

  private fun getBazelWorkspaceBlueprint(projectRoot: String = "foo") = BazelWorkspaceBlueprint(projectRoot)

}

