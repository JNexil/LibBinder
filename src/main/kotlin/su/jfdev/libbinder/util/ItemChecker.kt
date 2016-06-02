package su.jfdev.libbinder.util

import kotlin.reflect.declaredMemberProperties

/**
 * Jamefrus and his team on 01.06.2016.
 */

fun <T: Any> T.checkProperties(): T = apply {
    javaClass.kotlin.declaredMemberProperties.forEach {
        it.get(this)
    }
}