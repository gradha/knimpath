package es.elhaso.knimpath.internal


internal data class IntTuple(val a: Int, val b: Int)


internal data class PathIter(
    var i: Int,
    var prev: Int,
    var notFirst: Boolean
) {
    constructor() : this(0, 0, false)

    fun hasNext(x: String): Boolean =
        i < x.length

    fun next(x: String): IntTuple {
        prev = i
        if (!notFirst && x[i] in DirSepAltSep) {
            // absolute path
            i++
        } else {
            while (i < x.length && x[i] !in DirSepAltSep) i++
        }
        val result = if (i > prev) {
            IntTuple(prev, i - 1)
        } else if (hasNext(x)) {
            next(x)
        } else {
            IntTuple(0, 0)
        }
        // skip all separators
        while (i < x.length && x[i] in DirSepAltSep) i++
        notFirst = true
        return result
    }
}

internal fun String.isDot(bounds: IntTuple): Boolean =
    bounds.b == bounds.a && this[bounds.a] == '.'

internal fun String.isDotDot(bounds: IntTuple): Boolean =
    bounds.b == bounds.a + 1 && this[bounds.a] == '.' && this[bounds.a + 1] == '.'

internal fun String.isSlash(bounds: IntTuple): Boolean =
    bounds.b == bounds.a && this[bounds.a] in DirSepAltSep


fun addNormalizePath(x: String, state: JoinPathState, dirSep: Char = DirSep) {

    val x = if (doslikeFileSystem) { // Add Windows drive at start without normalization
        val (drive, file) = splitDrive(x)

        state.result.append(drive)
        state.result.map { c ->
            if (c in DirSepAltSep)
                dirSep
            else
                c
        }

        file
    } else {
        x
    }

    // state: 0th bit set if isAbsolute path. Other bits count
    // the number of path components.
    val it = PathIter()
    it.notFirst = (state.state shr 1) > 0
    if (it.notFirst) {
        while (it.i < x.length && x[it.i] in DirSepAltSep) it.i++
    }
    while (it.hasNext(x)) {
        val b = it.next(x)
        if ((state.state shr 1) == 0 && x.isSlash(b)) {
            if (state.result.isEmpty() || state.result.last() !in DirSepAltSep) {
                state.result.append(DirSep)
            }
            state.state = (state.state or 1)
        } else if (x.isDotDot(b)) {
            if ((state.state shr 1) >= 1) {
                var d = state.result.length
                // f/..
                // We could handle stripping trailing sep here: foo// => foo like this:
                // while (d-1) > (state and 1) and result[d-1] in {DirSep, AltSep}: dec d
                // but right now we instead handle it inside os.joinPath

                // strip path component: foo/bar => foo
                while ((d - 1) > (state.state and 1) && state.result[d - 1] !in DirSepAltSep)
                    d--
                if (d > 0) {
                    state.result.setLength(d - 1)
                    state.state -= 2
                }
            } else {
                if (state.result.isNotEmpty() && state.result.last() !in DirSepAltSep) {
                    state.result.append(DirSep)
                }
                state.result.append(x.substring(startIndex = b.a, endIndex = b.b + 1))
            }
        } else if (x.isDot(b)) {
            // Discard the dot
        } else if (b.b >= b.a) {
            if (state.result.isNotEmpty() && state.result.last() !in DirSepAltSep)
                state.result.append(DirSep)
            state.result.append(x.substring(startIndex = b.a, endIndex = b.b + 1))
            state.state += 2
        }
    }
    if (state.result.isEmpty() && x.isNotEmpty()) {
        state.result.clear()
        state.result.append(".")
    }
}

fun normalizePath(path: String, dirSep: Char = DirSep): String {
    val result = JoinPathState()
    addNormalizePath(path, result, dirSep)
    return result.result.toString()
}