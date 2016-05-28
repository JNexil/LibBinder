package su.jfdev.libbinder.items

import su.jfdev.libbinder.nullIfEmpty

data class Library(val group: String, val name: String, val version: String, val classifier: String?, val extension: String?) {
    val id: String get() = buildString {
        append("$group:$name:$version")
        if (classifier != null) append(":$classifier")
        if (extension != null) append("@$extension")
    }

    val map: Map<String, String> get() = mapOf("group" to group,
                                               "name" to name,
                                               "version" to version,
                                               "classifier" to classifier,
                                               "extension" to extension).mapNotNull {
        if (it.value == null) null
        else it.key to it.value!!
    }.toMap()

    companion object {
        fun from(id: String): Library {
            val withoutExtension = id.substringBefore("@")
            val extension = id.substringAfter("@", "").nullIfEmpty()
            val otherParts = withoutExtension.split(':')
            val (group, name, version) = otherParts
            val classifier = otherParts.getOrNull(3)
            return Library(group, name, version, classifier, extension)
        }

        fun from(map: Map<String, String>): Library
                = Library(map("group"), map("name"), map("version"), map["classifier"], map["extension"])

        private operator fun Map<String, String>.invoke(key: String) = this[key]
                ?: throw IllegalArgumentException("Parameter $key is required")
    }
}