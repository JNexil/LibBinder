package su.jfdev.libbinder

import groovy.lang.GroovyObject
import groovy.util.ConfigObject
import java.io.File
import java.io.IOException
import java.net.URL
import java.util.*

object Binding {
    @JvmStatic @JvmName("parse") operator
    fun get(mapOf: Map<String, String>): BindProvider = BindProvider(mapOf)

    @JvmStatic @JvmName("parse") operator
    fun get(properties: Properties): BindProvider = get(properties.map {
        it.key.toString() to it.value.toString()
    }.toMap())

    @JvmStatic @JvmName("parse") operator
    fun get(configObject: ConfigObject) = get(configObject.toProperties())

    @JvmStatic @JvmName("parse") operator
    fun get(file: File, way: ParsingWay): BindProvider = way(file.readText())

    @JvmStatic @JvmName("parse") operator
    fun get(file: File): BindProvider = get(file, ParsingWay[file.extension])

    @JvmStatic @JvmName("parse") operator
    fun get(url: URL): BindProvider {
        val extension = url.file.substringAfterLast('.', "")
        return get(url, ParsingWay[extension])
    }

    @JvmStatic @JvmName("parse") operator
    fun get(groovyObject: GroovyObject, vararg keys: String): BindProvider {
        val map = keys.associate {
            it to groovyObject.getProperty(it).toString()
        }
        return get(map)
    }

    @JvmStatic @JvmName("parse") operator
    fun get(url: URL, way: ParsingWay): BindProvider {
        val text: String
        try {
            text = url.readText()
            url.cache = text
        } catch(e: IOException) {
            println("Can't load from URL. Check file cache.")
            e.printStackTrace()
            text = url.cache ?: throw e
        }
        return way(text)
    }

    private var URL.cache: String?
        get() = cacheFile.readText()
        set(value) {
            if (value == null) cacheFile.delete()
            else cacheFile.writeText(value)
        }

    private val URL.cacheFile: File get() = File(libbinderDirectory, this.toString().filter { it.isLetterOrDigit() })

    var libbinderDirectory = File("libbinder").apply { mkdirs() }

}
