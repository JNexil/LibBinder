package su.jfdev.libbinder

import groovy.lang.GroovyObject
import groovy.lang.GroovyShell
import org.junit.Test
import su.jfdev.libbinder.ParsingWay.UnknownExtensionException
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.expect

/**
 * Jamefrus and his team on 27.05.2016.
 */

class BindingTest {
    @Test fun `is valid provider from String-String Map`() = validate {
        Binding[mapOf("alias" to ANY_ID)]
    }

    @Test fun `is valid provider from Properties`() = validate {
        ParsingWay.Properties("alias=$ANY_ID")
    }

    @Test fun `is valid provider from ConfigObject`() = validate {
        ParsingWay.GConfig("alias='$ANY_ID'")
    }

    @Test fun `is valid provider from json (by GSON)`() = validate {
        ParsingWay.Json("""
        {
            "alias": "$ANY_ID",
            "justInt": 4
        }
        """)
    }

    @Test fun `is valid provider from file`() = validate {
        val file = tempFile("testParseFile", "alias=$ANY_ID")
        Binding[file, ParsingWay.Properties]
    }

    @Test fun `is valid provider from url`() = validate {
        val url = tempFile("testParseUrl", "alias='$ANY_ID'").toURI().toURL()
        Binding[url, ParsingWay.GConfig]
    }

    @Test fun `is valid provider from file with autodetect way`() = validate {
        val file = tempFile("testAutoDetect", "alias=$ANY_ID", postfix = ".prop")
        Binding[file]
    }

    @Test fun `is valid provider from url with autodetect way`() = validate {
        val url = tempFile("testAutoDetect", "alias='$ANY_ID'", postfix = ".gconfig").toURI().toURL()
        Binding[url]
    }

    @Test fun `is valid provider from url (check file cache)`() = validate {
        val unsafe = tempFile("testAutoDetect", "alias='$ANY_ID'", postfix = ".gconfig")
        val url = unsafe.toURI().toURL()
        Binding[url]
        unsafe.delete()
        Binding[url]
    }

    @Test(expected = UnknownExtensionException::class)
    fun `if autodetect is impossible, throw exception`() = validate {
        val file = tempFile("testAutoDetect", "alias=$ANY_ID")
        Binding[file]
    }

    @Test fun `is valid provider from full filepath`() {
        val file = File("tempfileblablabla.prop")
        file.createNewFile()
        val fromDirectFile = Binding[file]
        val fromFilePath = Binding.file("tempfileblablabla.prop")
        assertEquals(fromDirectFile, fromFilePath)
    }

    @Test fun `is valid provider from GroovyObject`() = validate {
        val script: GroovyObject = GroovyShell().parse("alias = '$ANY_ID'").apply { run() }
        Binding[script, "alias"]
    }


    @Test(expected = MissingBindException::class)
    fun `if function's vararg non contains GroovyObject property's name, property is unknown`() = validate {
        val script: GroovyObject = GroovyShell().parse("alias = '$ANY_ID';other='any'").apply { run() }
        Binding[script, "other"]
    }

    private fun tempFile(prefix: String, text: String, postfix: String = ".tmp") = File.createTempFile(prefix, postfix).apply {
        delete(); createNewFile()
        writeText(text)
    }

    private fun validate(way: () -> BindProvider) = expect(ANY_ID) {
        way().library("alias").id
    }
}

