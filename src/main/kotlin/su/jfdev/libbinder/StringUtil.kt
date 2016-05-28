@file:Suppress("NOTHING_TO_INLINE")

package su.jfdev.libbinder

/**
 * Jamefrus and his team on 28.05.2016.
 */

inline fun String.nullIfEmpty(): String? = if (isEmpty()) null else this