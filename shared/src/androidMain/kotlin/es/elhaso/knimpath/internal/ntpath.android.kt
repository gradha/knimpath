package es.elhaso.knimpath.internal

/** Splits the drive from the path. For non [doslikeFileSystem] the path is returned untouched and
 * the drive part is the empty string.
 *
 * On Android you can consider this as being a NOP.
 */
actual fun splitDrive(p: String): SplitDriveResult {
    return SplitDriveResult(drive = "", path = p)
}
