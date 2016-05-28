package su.jfdev.libbinder

class MissingBindException(alias: String): Exception("Bind [$alias] is not exist")