package su.jfdev.libbinder

import groovy.lang.GroovyObject
import groovy.util.ConfigObject
import java.io.File
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
    fun get(url: URL, way: ParsingWay): BindProvider = way(url.readText())

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
}