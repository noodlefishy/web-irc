package io.cuttlefish

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.calllogging.*
import kotlinx.serialization.Serializable

@Serializable
data class ChatMessage(val sender: String, val content: String)

fun main() {
    embeddedServer(Netty, host = "localhost", port = 6700) {
        install(CallLogging)
        configureSerialization()
        configureSockets()
        configureRouting()


    }.start(wait = true)
}