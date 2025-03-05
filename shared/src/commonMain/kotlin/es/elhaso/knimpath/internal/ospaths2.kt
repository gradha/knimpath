package es.elhaso.knimpath.internal

data class PathResultIntern(
    val dir: String,
    val name: String,
    val ext: String,
)

data class JoinPathState(
    val result: StringBuilder,
    var state: Int,
) {
    constructor(head: String, tail: String): this(
        result = StringBuilder(head.length + tail.length),
        state = 0
    )

    constructor(): this(result = StringBuilder(), state = 0)
}

/**
 * Splits a filename into `(dir, name, extension)` tuple.
 *
 * `dir` does not end in DirSep_ unless it's `/`.
 * `extension` includes the leading dot.
 *
 * If `path` has no extension, `ext` is the empty string.
 * If `path` has no directory component, `dir` is the empty string.
 * If `path` has no filename component, `name` and `ext` are empty strings.
 *
 * @return [PathResultIntern]
 *
 * See also:
 * * `searchExtPos proc`_
 * * `extractFilename proc`_
 * * `lastPathPart proc`_
 * * `changeFileExt proc`_
 * * `addFileExt proc`_
 */
fun splitFile(path: String): PathResultIntern {
    var namePos = 0
    var dotPos = 0
    val stop = if (doslikeFileSystem) 0 else 0
    var resultDir: CharSequence = ""
    var resultName: CharSequence = ""
    var resultExt: CharSequence = ""

    for (i in (path.length - 1) downTo stop) {
        if (path[i] in DirSepAltSep || i == 0) {
            if (path[i] in DirSepAltSep) {
                resultDir = path.subSequence(0, if (i < 1) 1 else i)
                namePos = i + 1
            }
            if (dotPos > i) {
                resultName = path.subSequence(namePos, dotPos)
                resultExt = path.subSequence(dotPos, path.length)
            } else {
                resultName = path.subSequence(namePos, path.length)
            }
            break
        } else if (path[i] == ExtSep && i > 0 && i < path.length - 1 &&
            path[i - 1] !in DirSepAltSep &&
            path[i + 1] != ExtSep && dotPos == 0
        ) {
            dotPos = i
        }
    }

    return PathResultIntern(
        dir = StringBuilder(resultDir).toString(),
        name = resultName.toString(),
        ext = resultExt.toString()
    )
}
