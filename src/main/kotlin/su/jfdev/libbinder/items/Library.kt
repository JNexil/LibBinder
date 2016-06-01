package su.jfdev.libbinder.items

import su.jfdev.libbinder.IllegalFormatBindException
import su.jfdev.libbinder.nullIfEmpty
import su.jfdev.libbinder.util.mapOfNotNull
import su.jfdev.libbinder.util.nullable

data class Library(val map: Map<String, String>) {

    constructor(group: String, name: String, version: String, classifier: String?, extension: String?)
    : this(mapOfNotNull("group" to group, "name" to name, "version" to version, "classifier" to classifier,
                        "extension" to extension))

    private val nullableAccess = map.nullable()
    val group: String by map
    val name: String by map
    val version: String by map
    val classifier: String? by nullableAccess
    val extension: String? by nullableAccess

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