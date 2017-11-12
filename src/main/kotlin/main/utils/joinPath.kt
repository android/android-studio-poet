package main.utils

inline fun String.joinPath(addition: String) = this + "/" + addition
inline fun String.joinPaths(additions: List<String>) = this.joinPath(additions.joinToString("/"))