package su.jfdev.libbinder

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import su.jfdev.libbinder.items.Library
import su.jfdev.libbinder.items.Source
import su.jfdev.libbinder.util.checkProperties

data class BindProvider(val givenProperties: Map<String, String>) {

    fun libraries(groupAlias: String): Collection<Library> = groupOrSingle(groupAlias) { library(it) }

    fun sources(groupAlias: String): Collection<Source> = groupOrSingle(groupAlias) { source(it) }

    inline fun <R> groupOrSingle(groupAlias: String, crossinline fromAlias: (String) -> R): Collection<R> = try {
        group(groupAlias, fromAlias)
    } catch(e: BindException) {
        listOf(fromAlias(groupAlias))
    }

    inline fun <R> group(groupAlias: String, crossinline fromAlias: (String) -> R): Collection<R> {
        val evalText = item(groupAlias)
        val arrayItem: Array<*> = try {
            Gson().fromJson(evalText, Array<String>::class.java)
        } catch(e: JsonSyntaxException) {
            throw IllegalFormatBindException("Item [$groupAlias] should be array in groovy-like style")
        }
        return arrayItem.map {
            fromAlias(it.toString())
        }
    }

    fun library(alias: String): Library = try {
        Library.from(item(alias))
    } catch(e: BindException) {
        make(alias) { Library(it) }
    }

    fun source(alias: String): Source = try {
        Source(item(alias))
    } catch(e: MissingBindException) {
        make(alias) { Source(it) }
    }

    fun item(alias: String) = givenProperties[alias] ?: throw MissingBindException(alias)

    fun properties(alias: String): Map<String, String> {
        val properties = givenProperties.mapNotNull {
            val (key, value) = it
            val newKey = key.substringAfter("$alias.", "")
            if (newKey.isNotEmpty()) {
                newKey to value
            } else null
        }
        if (properties.isEmpty()) throw MissingBindException(alias)
        return properties.toMap()
    }

    inline fun <T: Any> make(alias: String, factory: (Map<String, String>) -> T): T {
        val properties = properties(alias)
        return try {
            factory(properties).checkProperties()
        } catch(any: Exception) {
            throw IllegalFormatBindException(alias)
        }
    }


}