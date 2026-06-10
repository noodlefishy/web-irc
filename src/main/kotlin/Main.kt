package io.cuttlefish

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*


fun main() {
    embeddedServer(Netty, host = "localhost", port = 6700) {
        install(WebSockets) {
            pingPeriodMillis = 15 * 1000L
        }


        routing {
            get("/") {
                call.respondText("Hello World!")
            }
            get("/ping") { call.respondText("Pong!") }

            webSocket("/chat") {
                send("Welcome to the chat!")

                for (frame in incoming) {
                    if (frame is Frame.Text) {
                        send("You said ${frame.readText()}")
                    }
                }
            }
        }

    }.start(wait = true)
}