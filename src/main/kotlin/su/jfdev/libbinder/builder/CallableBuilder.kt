package su.jfdev.libbinder.builder

import java.util.*
import kotlin.reflect.KCallable

class CallableBuilder<R>(val callable: KCallable<R>): Builder<R> {
    override val properties: MutableMap<String, String> = HashMap()

    override fun build(): R {
        val properties = callable.parameters.associate {
            it to properties[it.name]
        }
        return callable.callBy(properties)
    }

}

val <R> KCallable<R>.builder: Builder<R> get() = CallableBuilder(this)