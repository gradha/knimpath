package es.elhaso.knimpath

class MingwPlatform : Platform {
    override val name: String = "Windows something"
}

actual fun getPlatform(): Platform = MingwPlatform()