package su.jfdev.libbinder.builder

import su.jfdev.libbinder.BindException

interface Builder<T> {
    val properties: MutableMap<String, String>
    fun build(): T
    class FailedException(alias: String, type: Builder<*>, cause: Throwable): BindException("Failed building [${type.javaClass}][$alias]", cause)
}