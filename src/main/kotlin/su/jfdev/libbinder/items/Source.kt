package su.jfdev.libbinder.items

import su.jfdev.libbinder.util.mapOfNotNull
import su.jfdev.libbinder.util.nullable

data class Source(val properties: Map<String, String>) {
    val url: String by properties
    val username: String? by properties.nullable()
    val password: String? by properties.nullable()
    val type: String? by properties.nullable()

    constructor(url: String): this(url, null, null, null)

    constructor(url: String, username: String?,
                password: String?, type: String?): this(mapOfNotNull("url" to url,
                                                                     "password" to password,
                                                                     "username" to username,
                                                                     "type" to type))
}