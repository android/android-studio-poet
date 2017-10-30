package ui

import ui.models.ConfigPOJO

class DependencyValidator {
    fun isValid(config: ConfigPOJO): Boolean {
        //TODO Add check for cycle dependencies and proper reporting
        return correctAmountOfModules(config)
    }

    private fun correctAmountOfModules(config: ConfigPOJO): Boolean {
        return config.dependencies?.filter { it.to >= config.numModules || it.from >= config.numModules }?.isEmpty() ?: true
    }

}
