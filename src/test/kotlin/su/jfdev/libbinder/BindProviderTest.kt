package su.jfdev.libbinder

import org.junit.Test
import su.jfdev.libbinder.ANY_LIB.LIBRARY
import su.jfdev.libbinder.ANY_SRC.SOURCE
import su.jfdev.libbinder.items.Library
import su.jfdev.libbinder.items.Source
import kotlin.test.assertFailsWith
import kotlin.test.expect

/**
 * Jamefrus and his team on 27.05.2016.
 */

class BindProviderTest {

    fun makeGroup(first: String, second: String) = """["$first","$second"]"""

    @Test fun `is valid libraries`() {
        val other = Library.from(ANY_ID)
        listOf(LIBRARY, other).expectWith("group" to makeGroup("bean", "simple"), "simple" to ANY_ID, *ANY_LIB.beanPairs()) {
            it.libraries("group")
        }
    }

    @Test fun `is valid sources`() {
        val other = Source(ANY_URL)
        listOf(SOURCE, other).expectWith("group" to makeGroup("bean", "simple"), "simple" to ANY_URL, *ANY_SRC.beanPairs()) {
            it.sources("group")
        }
    }


    @Test fun `is valid library from id`() = ANY_ID.expectWith("22" to ANY_ID) { provider ->
        val library = provider.library("22")
        library.id
    }

    @Test fun `is valid library from props`() = LIBRARY.expectWith(*ANY_LIB.beanPairs()) { provider ->
        provider.library("bean")
    }

    @Test fun `is valid source from url`() = ANY_URL.expectWith("src" to ANY_URL) { provider ->
        val source: Source = provider.source("src")
        source.url
    }

    @Test fun `is valid source from props`() = SOURCE.expectWith(*ANY_SRC.beanPairs()) { provider ->
        provider.source("bean")
    }

    @Test fun `is valid item`() = "justItem".expectWith("item" to "justItem") { provider ->
        provider.item("item")
    }

    @Test fun `is valid properties map`() {
        val excepted = mapOf("one" to "ONE",
                             "two" to "TWO")
        excepted.expectWith("prop.one" to "ONE", "prop.two" to "TWO") { provider ->
            provider.properties("prop")
        }
    }

    @Test fun `if bind with alias is missing, throw exception`() = withProvider() { provider ->
        infix fun String.shouldMissing(get: BindProvider.(String) -> Unit) =
                assertFailsWith<MissingBindException>("$this() not thrown") { provider.get(MISSING_BIND) }

        "library" shouldMissing {
            library(it)
        }
        "source" shouldMissing {
            source(it)
        }
        "properties" shouldMissing {
            properties(it)
        }
    }

    @Test fun `if bean building is failed, throw exception`() = withProvider("fail.unknownProperty" to "anyValue") {
        assertFailsWith<IllegalFormatBindException> {
            it.library("fail")
        }
    }

    inline fun withProvider(vararg pairs: Pair<String, String>, block: (BindProvider) -> Unit) {
        val provider = Binding.get(pairs.toMap())
        block(provider)
    }

    inline fun <T> T.expectWith(vararg pairs: Pair<String, String>, crossinline block: (BindProvider) -> T) {
        withProvider(*pairs) {
            expect(this) {
                block(it)
            }
        }
    }
}

