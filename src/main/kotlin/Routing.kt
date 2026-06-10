package io.cuttlefish

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Server is up and running!")
        }

        webSocket("/chat") {
            sendSerialized(ChatMessage("Server", "Welcome to the chat!"))

            while (true) {
                val message = receiveDeserialized<ChatMessage>()
                println("Received from ${message.sender}: ${message.content}")

                sendSerialized(ChatMessage("Server", "I got your message: ${message.content}"))
            }
        }
    }
}