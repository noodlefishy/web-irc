package io.cuttlefish.client

import io.cuttlefish.ChatMessage
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.websocket.*
import io.ktor.serialization.kotlinx.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.websocket.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import io.ktor.http.ContentType.Application.Json as JsonContent

fun main() = runBlocking {

    val client = HttpClient(CIO) {
        install(WebSockets) {
            contentConverter = KotlinxWebsocketSerializationConverter(Json)
        }
        install(ContentNegotiation.toString()) {
            JsonContent
        }
    }

    println("Connecting to the chat server...")

    client.webSocket(host = "localhost", port = 6700, path = "/chat") {
        println("Connected! Type a message and press Enter:")
        val receiveJob = launch {
            for (frame in incoming) {
                if (frame is Frame.Text) {
                    val message = Json.decodeFromString<ChatMessage>(frame.readText())
                    println("[${message.sender}] ${message.content}")
                    print("> ")
                }
            }
        }

        while (true) {
            val message = readlnOrNull() ?: continue
            if (message.equals("/quit", ignoreCase = true)) {
                println("Disconnecting...")
                close(CloseReason(CloseReason.Codes.NORMAL, "Client exited"))
                break
            }
            sendSerialized(ChatMessage("Client", message))
//            send(Frame.Text(message))
        }

        receiveJob.cancel()
    }

    client.close()
}