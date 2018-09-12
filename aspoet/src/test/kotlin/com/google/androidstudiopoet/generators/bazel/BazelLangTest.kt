package com.google.androidstudiopoet.generators.bazel

import com.google.androidstudiopoet.testutils.assertEquals
import org.junit.Test

class BazelLangTest {

    @Test
    fun `assignment statement is converted to string correctly`() {
        val stmt = AssignmentStatement("foo", "\"bar\"")
        stmt.toString().assertEquals("foo = \"bar\"")
    }

    @Test
    fun `load statement is converted to string correctly`() {
        val stmt = LoadStatement("@foo//bar:baz.bzl", listOf("foo", "bar"))
        stmt.toString().assertEquals("""load("@foo//bar:baz.bzl", "foo", "bar")""")
    }

    @Test
    fun `comment is converted to string correctly`() {
        val comment = Comment("Foo bar baz")
        comment.toString().assertEquals("# Foo bar baz")
    }

    @Test
    fun `raw attribute is converted to string correctly`() {
        val attr = RawAttribute("foo", "[1, 2, 3]")
        attr.toString().assertEquals("foo = [1, 2, 3]")
    }

    @Test
    fun `string attribute is converted to string correctly`() {
        val attr = StringAttribute("foo", "bar")
        attr.toString().assertEquals("foo = \"bar\"")
    }

    @Test
    fun `target without attributes is converted to string correctly`() {
        val target = Target("foo_bar", listOf())
        target.toString().assertEquals("foo_bar()")
    }

    @Test
    fun `target with one attribute is converted to string correctly`() {
        val target = Target("foo_bar", listOf(RawAttribute("baz", "42")))
        target.toString().assertEquals("""foo_bar(
    baz = 42
)""")
    }

    @Test
    fun `target with two attributes is converted to string correctly`() {
        val target = Target(
            "foo_bar",
            listOf(
                RawAttribute("baz", "42"),
                StringAttribute("qux", "foo")
            ))
        target.toString().assertEquals("""foo_bar(
    baz = 42,
    qux = "foo"
)""")
    }

}

