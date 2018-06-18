package com.google.androidstudiopoet.models

data class TextViewBlueprint(val id: String, val stringName: String, val onClickAction: String?) {
    val hasAction = !onClickAction.isNullOrBlank()
}