package io.cuttlefish

import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.netty.handler.codec.compression.Zstd.cause
import kotlinx.coroutines.job
import java.util.Collections.synchronizedSet

val activeConnections = synchronizedSet(LinkedHashSet<DefaultWebSocketServerSession>())


fun Application.configureRouting() {
    routing {
        webSocket("/chat") {
            activeConnections += this@webSocket
            println("New User! Total users: ${activeConnections.size}")
            sendSerialized(ChatMessage("Server", "Welcome to the chat!"))



            coroutineContext.job.invokeOnCompletion {
                activeConnections.remove(this@webSocket)
                if (cause() == null) {
                    println("Clean disconnect")
                } else {
                    println("User disconnected abruptly")
                }
                println("Total users remaining: ${activeConnections.size}")

            }

            while (true) {
                val message = receiveDeserialized<ChatMessage>()
                println("Received from ${message.sender}: ${message.content}")

                activeConnections.forEach { connection ->
                    with(connection) {
                        sendSerialized(ChatMessage("Server", "I got your message: ${message.content}"))
                    }
                }


            }
        }
    }
}