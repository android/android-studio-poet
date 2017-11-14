package com.google.androidstudiopoet.utils

fun String.joinPath(addition: String) = this + "/" + addition
fun String.joinPaths(additions: List<String>) = this.joinPath(additions.joinToString("/"))