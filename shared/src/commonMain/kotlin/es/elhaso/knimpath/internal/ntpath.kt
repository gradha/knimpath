package es.elhaso.knimpath.internal

import es.elhaso.knimpath.Platform

data class SplitDriveResult(
    val drive: String,
    val path: String,
)

/** Splits the drive from the path. For non [doslikeFileSystem] the path is returned untouched and
 * the drive part is the empty string.
 */
expect fun splitDrive(p: String): SplitDriveResult