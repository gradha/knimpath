import es.elhaso.knimpath.Path
import es.elhaso.knimpath.Platform
import es.elhaso.knimpath.div
import es.elhaso.knimpath.getPlatform
import es.elhaso.knimpath.internal.defined_posix
import es.elhaso.knimpath.internal.normalizePath
import es.elhaso.knimpath.internal.splitFile
import es.elhaso.knimpath.joinPath
import es.elhaso.knimpath.normalizePathEnd
import es.elhaso.knimpath.splitFile
import kotlin.test.Test
import kotlin.test.assertEquals

private inline fun scope(block: () -> Unit) {
    block()
}

class FooTest {

    @Test
    fun identityTest() {
        val platform = getPlatform()
        val name = platform.name
        println("Showing platform name '$name'")
    }
    @Test
    fun splitFileTests() {
        val p = "Foo" / "bar" / "baz.txt"
        assertEquals(p.value, "Foo/bar/baz.txt")
        val result = splitFile(p)

        scope {
            val (dir, name, ext) = splitFile("usr/local/nimc.html")
            assertEquals("usr/local", dir)
            assertEquals("nimc", name)
            assertEquals(".html", ext)
        }
        scope {
            val (dir, name, ext) = splitFile("/usr/local/os")
            assertEquals("/usr/local", dir)
            assertEquals("os", name)
            assertEquals("", ext)
        }
        scope {
            val (dir, name, ext) = splitFile("/usr/local/")
            assertEquals("/usr/local", dir)
            assertEquals("", name)
            assertEquals("", ext)
        }
        scope {
            val (dir, name, ext) = splitFile("/tmp.txt")
            assertEquals("/", dir)
            assertEquals("tmp", name)
            assertEquals(".txt", ext)
        }
    }

    @Test
    fun joinPathTests() {
        assertEquals(defined_posix, true)
        assertEquals(joinPath("usr", "lib"), "usr/lib")
        assertEquals(joinPath("usr", "lib/"), "usr/lib/")
        assertEquals(joinPath("usr", ""), "usr")
        assertEquals(joinPath("usr/", ""), "usr/")
        assertEquals(joinPath("", ""), "")
        assertEquals(joinPath("", "lib"), "lib")
        assertEquals(joinPath("", "/lib"), "/lib")
        assertEquals(joinPath("usr/", "/lib"), "usr/lib")
        assertEquals(joinPath("usr/lib", "../bin"), "usr/bin")
    }

    @Test
    fun normalizePathEndTests() {
        assertEquals(defined_posix, true)
        assertEquals(normalizePathEnd("/lib//.//", trailingSep = true), "/lib/")
        assertEquals(normalizePathEnd("lib/./.", trailingSep = false), "lib")
        assertEquals(normalizePathEnd(".//./.", trailingSep = false), ".")
        assertEquals(normalizePathEnd("", trailingSep = true), "") // not / !
        assertEquals(normalizePathEnd("/", trailingSep = false), "/") // not "" !

    }

    @Test
    fun normalizePathTests() {
        assertEquals("foo/baz", normalizePath("./foo//bar/../baz"))
    }

    @Test
    fun genericUsage() {
        val path = Path("foo") / "bar" / "test.txt"
        val (dir, name, ext) = splitFile(path)
        println("Got '${dir.value}' / '$name' + '$ext'")
        assertEquals(Path("foo/bar/test.txt"), dir / name + ext)
        assertEquals(Path("foo/bar/test"), dir / name)
    }
}