package su.jfdev.libbinder.builder

import su.jfdev.libbinder.items.Library
import su.jfdev.libbinder.nullIfEmpty
import java.util.*

class LibraryBuilder: Builder<Library> {
    override val properties: MutableMap<String, String> = HashMap()
    val group by properties
    val name by properties
    val version by properties
    val classifier by properties.withDefault { "" }
    val extension by properties.withDefault { "" }

    override fun build() = Library(group, name, version, classifier.nullIfEmpty(), extension.nullIfEmpty())

}