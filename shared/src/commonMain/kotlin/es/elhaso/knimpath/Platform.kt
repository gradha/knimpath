package es.elhaso.knimpath

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform