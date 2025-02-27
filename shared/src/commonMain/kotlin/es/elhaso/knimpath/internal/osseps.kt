package es.elhaso.knimpath.internal


const val doslikeFileSystem = false

/**
 * The constant character used by the operating system to refer to the
 * current directory.
 *
 * For example: `'.'` for POSIX.
 */
const val CurDir = '.'

/**
 * The constant string used by the operating system to refer to the
 * parent directory.
 *
 * For example: `".."` for POSIX.
 */
const val ParDir = ".."

/**
 * The character used by the operating system to separate pathname
 * components, for example: `'/'` for POSIX, `':'` for the classic
 * Macintosh, and `'\\'` on Windows.A
 *
 * See also [AltSep].
 */
val DirSep = if (doslikeFileSystem) '\\' else '/'

/**
 * An alternative character used by the operating system to separate
 * pathname components, or the same as [DirSep] if only one separator
 * character exists. This is set to `'/'` on Windows systems
 * where DirSep_ is a backslash (`'\\'`).
 */
val AltSep = if (doslikeFileSystem) '/' else DirSep

/** Convenience combination of [DirSep] and [AltSep] as a set.
 */
val DirSepAltSep = setOf(DirSep, AltSep)

/**
 * The character conventionally used by the operating system to separate
 * search path components (as in PATH), such as `':'` for POSIX
 * or `';'` for Windows.
 */
val PathSep = if (doslikeFileSystem) ';' else ':'

/**
 * True if the file system is case sensitive, false otherwise. Used by
 * `cmpPaths proc`_ to compare filenames properly.
 */
val FileSystemCaseSensitive =
    if (defined_macosx || doslikeFileSystem) false
    else true

/**
 * The file extension of native executables. For example:
 * `""` for POSIX, `"exe"` on Windows (without a dot).
 */
val ExeExt = if (doslikeFileSystem) "exe" else ""

/**
 * The file extension of a script file. For example: `""` for POSIX,
 * `"bat"` on Windows.
 */
val ScriptExt = if (doslikeFileSystem) "bat" else ""

/**
 * The character which separates the base filename from the extension;
 * for example, the `'.'` in `os.nim`.
 */
val ExtSep = '.'