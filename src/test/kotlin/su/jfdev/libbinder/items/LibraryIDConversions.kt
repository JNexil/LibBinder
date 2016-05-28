package su.jfdev.libbinder.items

import org.junit.Test
import su.jfdev.libbinder.ANY_LIB
import su.jfdev.libbinder.ANY_LIB.CLASSIFIER
import su.jfdev.libbinder.ANY_LIB.EXTENSION
import su.jfdev.libbinder.ANY_LIB.GROUP
import su.jfdev.libbinder.ANY_LIB.LIBRARY
import su.jfdev.libbinder.ANY_LIB.NAME
import su.jfdev.libbinder.ANY_LIB.VERSION
import kotlin.test.assertEquals
import kotlin.test.expect

/**
 * Jamefrus and his team on 28.05.2016.
 */
class `Library to ID` {
    @Test fun `is valid library id of (group,name,version)`() =
            Library(GROUP, NAME, VERSION, null, null).checkID("$GROUP:$NAME:$VERSION")

    @Test fun `is valid library id of (group,name,version, classifier)`() =
            Library(GROUP, NAME, VERSION, CLASSIFIER, null).checkID("$GROUP:$NAME:$VERSION:$CLASSIFIER")

    @Test fun `is valid library id of (group,name,version, extension)`() =
            Library(GROUP, NAME, VERSION, null, EXTENSION).checkID("$GROUP:$NAME:$VERSION@$EXTENSION")

    @Test fun `is valid library id of (group,name,version, classifier, extension)`() =
            LIBRARY.checkID(ANY_LIB.ID)

    private fun Library.checkID(excepted: String) = expect(excepted) { id }
}

class `Library from ID` {
    @Test fun `is valid library from id of (group,name,version)`() =
            "$GROUP:$NAME:$VERSION".checkBase().checkNoClassifier().checkNoExtension()

    @Test fun `is valid library from id of (group,name,version, classifier)`() =
            "$GROUP:$NAME:$VERSION:$CLASSIFIER".checkBase().checkClassifier().checkNoExtension()

    @Test fun `is valid library from id of (group,name,version, extension)`() =
            "$GROUP:$NAME:$VERSION@$EXTENSION".checkBase().checkNoClassifier().checkExtension()

    @Test fun `is valid library from id of (group,name,version, classifier, extension)`() =
            ANY_LIB.ID.checkBase().checkClassifier().checkExtension()

    private fun String.checkBase() = Library.from(this).apply {
        assertEquals(GROUP, group)
        assertEquals(NAME, name)
        assertEquals(VERSION, version)
    }

    private fun Library.checkClassifier() = apply { assertEquals(CLASSIFIER, classifier) }
    private fun Library.checkNoClassifier() = apply { assertEquals(null, classifier) }
    private fun Library.checkExtension(): Unit = assertEquals(EXTENSION, extension)
    private fun Library.checkNoExtension(): Unit = assertEquals(null, extension)
}