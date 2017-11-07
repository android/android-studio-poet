package main.utils



fun Iterable<String>.fold() = this.fold("") { acc, next -> acc + next }