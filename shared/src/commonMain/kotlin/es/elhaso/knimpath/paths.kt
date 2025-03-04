package es.elhaso.knimpath

import es.elhaso.knimpath.internal.DirSep
import es.elhaso.knimpath.internal.DirSepAltSep
import kotlin.jvm.JvmInline

@JvmInline
value class Path(val value: String) {

    operator fun div(x: String): Path {
        return Path("$value/$x")
    }

    operator fun div(x: Path): Path {
        return Path("$value/${x.value}")
    }
}

operator fun String.div(x: String): Path {
    return Path("$this/$x")
}


/** Contains the leading path, base filename and extension after calling [splitFile].
 */
data class PathResult(
    val dir: Path,
    val name: Path,
    val ext: String,
)

/** Ensures ``path`` has exactly 0 or 1 trailing [es.elhaso.knimpath.internal.DirSep], depending on
 * ``trailingSep``, and taking care of edge cases: it preserves
 * whether a path is absolute or relative, and makes sure trailing sep
 * is [es.elhaso.knimpath.internal.DirSep], not
 * [es.elhaso.knimpath.internal.AltSep]. Trailing `/.` are compressed,
 * see examples.
 */
fun normalizePathEnd(path: StringBuilder, trailingSep: Boolean = true) {
    if (path.isEmpty()) return

    var i = path.length
    while (i >= 1) {
        if (path[i - 1] in DirSepAltSep) i--
        else if (path[i - 1] == '.' && i >= 2 && path[i - 2] in DirSepAltSep) i--
        else break
    }
    if (trailingSep) {
        // foo// => foo
        path.setLength(i)
        // foo => foo/
        path.append(DirSep)
    } else if (i > 0) {
        // foo// => foo
        path.setLength(i)
    } else {
        // // => / (empty case was already taken care of)
        path.setLength(1)
        path[0] = DirSep
    }
}

fun normalizePathEnd(string: String, trailingSep: Boolean = true): String {
    val result = StringBuilder(string)
    normalizePathEnd(result, trailingSep)
    return result.toString()
}

/** Joins two directory names into a path.
 *
 * @return Normalized path concatenation of `head` and `tail`,
 * preserving whether or not `tail` has a trailing slash (or, if tail
 * if empty, whether head has one).
 *
 * See also:
 * * `joinPath(parts: varargs[string]) proc`_
 * * `/ proc`_
 * * `splitPath proc`_
 * * `uri.combine proc <uri.html#combine,Uri,Uri>`_
 * * `uri./ proc <uri.html#/,Uri,string>`_
 */
fun joinPath(head: String, tail: String): String {

    return ""
    /*
result = newStringOfCap(head.len + tail.len)
var state = 0
joinPathImpl(result, state, head)
joinPathImpl(result, state, tail)
when false:
if len(head) == 0:
result = tail
elif head[len(head)-1] in { DirSep, AltSep }:
if tail.len > 0 and tail[0] in { DirSep, AltSep }:
result = head & substr(tail, 1)
else:
result = head & tail
else:
if tail.len > 0 and tail[0] in { DirSep, AltSep }:
result = head & tail
else:
result = head & DirSep & tail
     */
}

/** Splits a path into `(dir, name, extension)` components.
 *
 * `dir` does not end in [es.elhaso.knimpath.internal.DirSep] unless
 * it's `/`. `extension` includes the leading dot.
 *
 * If `path` has no extension, `ext` is the empty string.
 * If `path` has no directory component, `dir` is the empty string.
 * If `path` has no filename component, `name` and `ext` are empty strings.
 *
 * @return A [PathResult] data class which can be decomposed into `(dir, name, extension)`
 * components.
 *
 * See also:
 * * `searchExtPos proc`_
 * * `extractFilename proc`_
 * * `lastPathPart proc`_
 * * `changeFileExt proc`_
 * * `addFileExt proc`_
 */
fun splitFile(path: Path): PathResult {
    val result = es.elhaso.knimpath.internal.splitFile(path.value)
    return PathResult(Path(result.dir), Path(result.name), result.ext)
}