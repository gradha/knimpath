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



data class PathResult(
    val dir: Path,
    val name: Path,
    val ext: String,
)
