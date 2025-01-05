import es.elhaso.knimpath.div
import kotlin.test.Test
import kotlin.test.assertEquals

class FooTest {
    @Test
    fun commonTest() {
        val p = "Foo" / "bar" / "baz"
        print("Path '$p'")
        assertEquals(p.value, "Foo/bar/baz")
    }
}