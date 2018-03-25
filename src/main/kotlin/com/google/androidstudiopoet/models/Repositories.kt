package com.google.androidstudiopoet.models

sealed class Repository {
    data class Named(val name: String): Repository()
}