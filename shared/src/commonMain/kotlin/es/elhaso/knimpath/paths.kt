package es.elhaso.knimpath

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


/** Splits a path into `(dir, name, extension)` components.
 *
 * `dir` does not end in [es.elhaso.knimpath.internal.DirSep] unless it's `/`. `extension`
 * includes the leading dot.
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