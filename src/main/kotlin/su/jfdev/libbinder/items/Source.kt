package su.jfdev.libbinder.items

data class Source(val url: String, val username: String?, val password: String?) {
    constructor(url: String): this(url, null, null)
}