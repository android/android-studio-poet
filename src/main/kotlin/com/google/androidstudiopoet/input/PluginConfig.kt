package com.google.androidstudiopoet.input

data class PluginConfig(val id: String, val taskName: String? = null, val taskBody: List<String>? = null)