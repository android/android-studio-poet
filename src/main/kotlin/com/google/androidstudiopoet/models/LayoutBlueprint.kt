package com.google.androidstudiopoet.models

class LayoutBlueprint(val filePath: String,
                      val stringNamesToUse: List<String>,
                      val imagesToUse: List<String>,
                      val layoutsToInclude: List<String>)