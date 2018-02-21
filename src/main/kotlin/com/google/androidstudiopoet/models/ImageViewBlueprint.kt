package com.google.androidstudiopoet.models

data class ImageViewBlueprint(val imageName: String, val onClickAction: String?) {
    val hasAction = !onClickAction.isNullOrBlank()
}