package main

interface Blueprint

interface GenerationResult

interface Generator<in T : Blueprint, out K : GenerationResult> {
    fun generate(blueprint: T): K
}