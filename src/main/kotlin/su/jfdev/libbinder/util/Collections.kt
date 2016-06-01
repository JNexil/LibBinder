@file:Suppress("NOTHING_TO_INLINE")

package su.jfdev.libbinder.util

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Jamefrus and his team on 01.06.2016.
 */

inline fun <K, V> Map<K, V?>.skipNullValues() = mapNotNull {
    if (it.value == null) null
    else it.key to it.value!!
}.toMap()

inline fun <K, V> mapOfNotNull(vararg pairs: Pair<K, V?>) = mapOf(*pairs).skipNullValues()

inline fun <V> Map<String, V>.nullable() = NullableMapProperty<Any?, V>(this)

class NullableMapProperty<R, V>(val map: Map<String, V>): ReadOnlyProperty<R, V?> {
    override operator fun getValue(thisRef: R, property: KProperty<*>) = map[property.name]
}