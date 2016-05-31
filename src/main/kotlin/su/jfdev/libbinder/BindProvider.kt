package su.jfdev.libbinder

import groovy.util.Eval
import su.jfdev.libbinder.builder.Builder
import su.jfdev.libbinder.builder.Builder.FailedException
import su.jfdev.libbinder.builder.LibraryBuilder
import su.jfdev.libbinder.builder.builder
import su.jfdev.libbinder.items.Library
import su.jfdev.libbinder.items.Source
import kotlin.reflect.primaryConstructor

data class BindProvider(val givenProperties: Map<String, String>) {

    fun libraries(groupAlias: String): Collection<Library> = groupOrSingle(groupAlias) { library(it) }

    fun sources(groupAlias: String): Collection<Source> = groupOrSingle(groupAlias) { source(it) }

    inline fun <R> groupOrSingle(groupAlias: String, crossinline aliasToR: (String) -> R): Collection<R> = try {
        listOf(aliasToR(groupAlias))
    } catch(e: MissingBindException) {
        group(groupAlias, aliasToR)
    }

    inline fun <R> group(groupAlias: String, crossinline aliasToR: (String) -> R): Collection<R> {
        val evalText = item(groupAlias) + " as String[]"
        val arrayItem: Array<*> = Eval.me(evalText) as? Array<*>
                ?: throw IllegalArgumentException("item [$groupAlias] should be array in groovy-like style")
        return arrayItem.map {
            aliasToR(it.toString())
        }
    }

    fun library(alias: String): Library = try {
        Library.from(item(alias))
    } catch(e: MissingBindException) {
        LibraryBuilder().make(alias)
    }

    fun source(alias: String): Source = try {
        Source(item(alias))
    } catch(e: MissingBindException) {
        Source::class.primaryConstructor!!.builder.make(alias)
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

    fun <T> Builder<T>.make(alias: String): T = build(this, alias)

    fun <T> build(builder: Builder<T>, alias: String): T {
        properties(alias).forEach { declared ->
            builder.properties += declared.toPair()
        }
        return try {
            builder.build()
        } catch(any: Exception) {
            throw FailedException(alias, builder, any)
        }
    }


}