package ui.generators

import ui.generators.packages.JavaGenerator
import ui.generators.packages.KotlinGenerator
import ui.models.ConfigPOJO
import java.io.File

class AndroidPackagesGenerator(javaGenerator: JavaGenerator,
                               kotlinGenerator: KotlinGenerator) {
    fun writePackages(configPOJO: ConfigPOJO, index: Int, where: String, file: File) {
        // generate manifest

        // sources == activities

        // resources = layouts/views
    }
}