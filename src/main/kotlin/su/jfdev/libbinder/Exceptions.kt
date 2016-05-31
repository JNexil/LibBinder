package su.jfdev.libbinder

/**
 * Jamefrus and his team on 31.05.2016.
 */
open class BindException: RuntimeException {
    constructor(message: String, cause: Throwable): super(message, cause)

    constructor(message: String): super(message)
}

class MissingBindException(alias: String): BindException("Bind [$alias] is not exist")
class IllegalFormatBindException(message: String): BindException(message)