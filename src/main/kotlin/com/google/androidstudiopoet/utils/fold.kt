package com.google.androidstudiopoet.utils



fun Iterable<String>.fold() = this.fold("") { acc, next -> acc + next }