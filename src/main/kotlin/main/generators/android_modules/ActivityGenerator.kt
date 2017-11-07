package main.generators.android_modules

import main.GenerationResult
import main.models.AndroidModuleBlueprint

class ActivityGenerator {

    /**
     * generates activity classes by blueprint, list of layouts and methods to call.
     */
    fun generate(blueprint: AndroidModuleBlueprint, layouts: List<String>, methodsToCall: List<String>): ActivityGenerationResult {
        return ActivityGenerationResult(0.until(blueprint.numOfActivities).map { "Activity$it" })
    }

}

data class ActivityGenerationResult(val activityNames: List<String>): GenerationResult