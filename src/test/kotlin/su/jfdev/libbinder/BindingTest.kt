package su.jfdev.libbinder

import groovy.lang.GroovyObject
import groovy.lang.GroovyShell
import org.junit.Test
import su.jfdev.libbinder.ParsingWay.UnknownExtensionException
import java.io.File
import kotlin.test.expect

/**
 * Jamefrus and his team on 27.05.2016.
 */

class BindingTest {
    @Test fun `is valid provider from String-String Map`() = validate {
        Binding.get(mapOf("alias" to ANY_ID))
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
        Binding.get(file, ParsingWay.Properties)
    }

    @Test fun `is valid provider from url`() = validate {
        val url = tempFile("testParseUrl", "alias='$ANY_ID'").toURI().toURL()
        Binding.get(url, ParsingWay.GConfig)
    }

    @Test fun `is valid provider from file with autodetect way`() = validate {
        val file = tempFile("testAutoDetect", "alias=$ANY_ID", postfix = ".prop")
        Binding.get(file)
    }

    @Test fun `is valid provider from url with autodetect way`() = validate {
        val url = tempFile("testAutoDetect", "alias='$ANY_ID'", postfix = ".gconfig").toURI().toURL()
        Binding.get(url)
    }

    @Test(expected = UnknownExtensionException::class)
    fun `if autodetect is impossible, throw exception`() = validate {
        val file = tempFile("testAutoDetect", "alias=$ANY_ID")
        Binding.get(file)
    }

    @Test fun `is valid provider from GroovyObject`() = validate {
        val script: GroovyObject = GroovyShell().parse("alias = '$ANY_ID'").apply { run() }
        Binding.get(script, "alias")
    }


    @Test(expected = MissingBindException::class)
    fun `if function's vararg non contains GroovyObject property's name, property is unknown`() = validate {
        val script: GroovyObject = GroovyShell().parse("alias = '$ANY_ID';other='any'").apply { run() }
        Binding.get(script, "other")
    }

    private fun tempFile(prefix: String, text: String, postfix: String = ".tmp") = File.createTempFile(prefix, postfix).apply {
        delete(); createNewFile()
        writeText(text)
    }

    private fun validate(way: () -> BindProvider) = expect(ANY_ID) {
        way().library("alias").id
    }
}

