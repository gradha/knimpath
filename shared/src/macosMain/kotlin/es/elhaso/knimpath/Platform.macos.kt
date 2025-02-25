package es.elhaso.knimpath

class MacosPlatform : Platform {
    override val name: String = "Macosx something?"
}

actual fun getPlatform(): Platform = MacosPlatform()