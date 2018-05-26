package com.google.androidstudiopoet.input

/**
 * Configuration of a plugin applied to a module.
 *
 * @param id Id of the plugin that will be used in the gradle script to apply it
 * @param taskName Name of the gradle task that is used to customize plugin
 * @param taskBody Body of the gradle task that is used to customize plugin
 */
data class PluginConfig(val id: String, val taskName: String? = null, val taskBody: List<String>? = null)