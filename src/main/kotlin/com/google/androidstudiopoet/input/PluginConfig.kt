/*
Copyright 2018 Google Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.google.androidstudiopoet.input

/**
 * Configuration of a plugin applied to a module.
 *
 * @param id Id of the plugin that will be used in the gradle script to apply it
 * @param taskName Name of the gradle task that is used to customize plugin
 * @param taskBody Body of the gradle task that is used to customize plugin
 */
data class PluginConfig(val id: String, val taskName: String? = null, val taskBody: List<String>? = null)