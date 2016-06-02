package su.jfdev.libbinder.items

import su.jfdev.libbinder.IllegalFormatBindException
import su.jfdev.libbinder.util.mapOfNotNull
import su.jfdev.libbinder.util.nullIfEmpty
import su.jfdev.libbinder.util.nullable

data class Library(val properties: Map<String, String>) {

    constructor(group: String, name: String, version: String, classifier: String?, extension: String?)
    : this(mapOfNotNull("group" to group, "name" to name, "version" to version, "classifier" to classifier,
                        "extension" to extension))

    val group: String by properties
    val name: String by properties
    val version: String by properties
    val classifier: String? by properties.nullable()
    val extension: String? by properties.nullable()

    val id: String get() = buildString {
        append("$group:$name:$version")
        if (classifier != null) append(":$classifier")
        if (extension != null) append("@$extension")
    }

    companion object {
        fun from(id: String): Library {
            val withoutExtension = id.substringBefore("@")
            val extension = id.substringAfter("@", "").nullIfEmpty()
            val otherParts = withoutExtension.split(':')
            if (otherParts.size < 3) throw IllegalFormatBindException("Format of bind declaration is unreadable")
            val (group, name, version) = otherParts
            val classifier = otherParts.getOrNull(3)
            return Library(group, name, version, classifier, extension)
        }

        @Suppress("NOTHING_TO_INLINE", "unused") inline fun from(map: Map<String, String>) = Library(map)
    }
}