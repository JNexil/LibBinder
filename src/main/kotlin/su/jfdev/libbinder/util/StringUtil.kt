@file:Suppress("NOTHING_TO_INLINE")

package su.jfdev.libbinder.util

/**
 * Jamefrus and his team on 28.05.2016.
 */

inline fun String.nullIfEmpty(): String? = if (isEmpty()) null else this