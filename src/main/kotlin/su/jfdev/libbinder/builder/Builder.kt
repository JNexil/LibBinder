package su.jfdev.libbinder.builder

interface Builder<T> {
    val properties: MutableMap<String, String>
    fun build(): T
    class FailedException(alias: String, type: Builder<*>, cause: Throwable): Exception("Failed building [${type.javaClass}][$alias]", cause)
}