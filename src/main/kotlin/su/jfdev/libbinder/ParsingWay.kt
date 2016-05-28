package su.jfdev.libbinder

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import groovy.util.ConfigSlurper
import java.util.*

enum class ParsingWay(val extensions: Iterable<String>) {
    GConfig("gconfig") {
        override fun invoke(text: String): BindProvider {
            val configObject = ConfigSlurper().parse(text)
            return Binding.get(configObject)
        }
    },
    Properties("properties", "prop") {
        override fun invoke(text: String): BindProvider {
            val properties = Properties()
            properties.load(text.reader())
            return Binding.get(properties)
        }
    },
    GScript("groovy", "gscript") {
        override fun invoke(text: String) = TODO()
    },
    Json("json") {
        override fun invoke(text: String): BindProvider {
            val jsonObject = Gson().fromJson(text, JsonObject::class.java)
            val properties = jsonObject.entrySet().flatMap {
                recursive(it.key, it.value)
            }.toMap()
            return Binding.get(properties)
        }

        fun recursive(key: String, value: JsonElement): List<Pair<String, String>> {
            if (value.isJsonPrimitive) return listOf(key to value.asJsonPrimitive.asString)
            if (value.isJsonArray) return listOf(key to value.asJsonArray.joinToString { it.asString })
            if (value.isJsonObject) return value.asJsonObject.entrySet().flatMap {
                recursive("$key.${it.key}", it.value)
            }
            return emptyList()
        }
    };

    constructor(): this(emptyList())

    constructor(vararg extensions: String): this(extensions.toList())

    operator abstract fun invoke(text: String): BindProvider
    @Suppress("unused") //API
    fun parse(text: String) = invoke(text)

    companion object {
        operator fun get(extension: String) = ParsingWay.values().find {
            it.extensions.contains(extension)
        } ?: throw UnknownExtensionException(extension)
    }

    class UnknownExtensionException(extension: String): Exception(
            """Extension [$extension] is unsupported. Please, specify ParsingWay or change extension.
            Supported: ${ParsingWay.values().flatMap { it.extensions }.joinToString { ".$it" }}"""
    )


}