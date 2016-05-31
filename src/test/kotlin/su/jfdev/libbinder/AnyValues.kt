package su.jfdev.libbinder

import su.jfdev.libbinder.items.Library
import su.jfdev.libbinder.items.Source

/**
 * Jamefrus and his team on 27.05.2016.
 */


const val ANY_ID = "groupId:artifactId:version"

object ANY_LIB {
    const val GROUP = "anyGroup"
    const val NAME = "anyName"
    const val VERSION = "anyVersion"
    const val CLASSIFIER = "anyCl"
    const val EXTENSION = "extraExtEx"
    val LIBRARY = Library(GROUP, NAME, VERSION, CLASSIFIER, EXTENSION)
    const val ID: String = "$GROUP:$NAME:$VERSION:$CLASSIFIER@$EXTENSION"

    fun beanPairs() = arrayOf("bean.group" to ANY_LIB.GROUP,
                              "bean.name" to ANY_LIB.NAME,
                              "bean.version" to ANY_LIB.VERSION,
                              "bean.extension" to ANY_LIB.EXTENSION,
                              "bean.classifier" to ANY_LIB.CLASSIFIER)
}

object ANY_SRC {
    const val URL = ANY_URL
    const val USERNAME = "ANY_USERNAME"
    const val PASSWORD = "pass"
    val SOURCE = Source(URL, USERNAME, PASSWORD, null)

    fun beanPairs() = arrayOf("bean.url" to URL,
                              "bean.username" to USERNAME,
                              "bean.password" to PASSWORD)
}

const val ANY_URL = "it's url"
const val MISSING_BIND = "missing"