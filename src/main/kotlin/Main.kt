package io.cuttlefish

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main() {
    embeddedServer(Netty, host = "localhost", port = 6700) {
        routing {
            get("/") {
                call.respondText("Hello World!")
            }
            get("/ping") { call.respondText("Pong!") }

        }

    }.start(wait = true)
}