package es.elhaso.knimpath.internal

import kotlin.math.min

private const val sep = '\\'
private const val unc = "\\\\?\\UNC" // Length is 7

/** Splits the drive from the path. For non [doslikeFileSystem] the path is returned untouched and
 * the drive part is the empty string.
 */
actual fun splitDrive(p: String): SplitDriveResult {

    if (p.length < 2) return SplitDriveResult("", p)

    val normp = p.replace('/', sep)
    if (p.length > 2 && normp[0] == sep && normp[1] == sep && normp[2] != sep) {

        val idx = min(8, normp.length)
        val start = if (unc == normp.substring(0, idx).stripTrailingSep().uppercase()) {
            8
        } else {
            2
        }
        val index = normp.find(sep, start)
        if (index < 0) return SplitDriveResult("", p)

        var index2 = normp.find(sep, index + 1)

        // a UNC path can't have two slashes in a row (after the initial two)
        if (index2 == index + 1) return SplitDriveResult("", p)

        return SplitDriveResult(
            drive = p.substring(startIndex = 0, endIndex = index2),
            path = p.substring(index2)
        )
    } else if (p[1] == ':') {
        return SplitDriveResult(
            drive = "${p[0]}${p[1]}",
            path = p.substring(startIndex = 2)
        )
    } else {
        return SplitDriveResult("", p)
    }
}

private fun String.stripTrailingSep(): String {
    var result = this
    while (result.lastOrNull() == sep)
        result = result.substring(0, result.length - 1)
    return result
}

private fun String.find(c: Char, start: Int): Int {
    var pos = start
    while (pos < length && this[pos] != c) pos++

    if (pos < length && this[pos] == c) return pos
    else return -1
}