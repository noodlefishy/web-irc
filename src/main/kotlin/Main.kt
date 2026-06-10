package io.cuttlefish

import io.ktor.serialization.kotlinx.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class ChatMessage(val sender: String, val content: String)

fun main() {
    embeddedServer(Netty, host = "localhost", port = 6700) {
        install(CallLogging)

        install(ContentNegotiation) {
            json()
        }

        install(WebSockets) {
            pingPeriodMillis = 15 * 1000L
            contentConverter = KotlinxWebsocketSerializationConverter(Json)

        }


        routing {


            webSocket("/chat") {
                sendSerialized(ChatMessage("Server", "Welcome to the chat!"))

                while (true) {
                    val message = receiveDeserialized<ChatMessage>()
                    println("Received from ${message.sender}: ${message.content}")

                    sendSerialized(ChatMessage("Server", "I got your message: ${message.content}"))
                }
            }
        }


    }.start(wait = true)
}