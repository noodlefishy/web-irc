package io.cuttlefish.client

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import java.util.Scanner

fun main() = runBlocking {

    val client = HttpClient(CIO) {
        install(WebSockets)
    }

    println("Connecting to the chat server...")

    client.webSocket(host = "localhost", port = 6700, path = "/chat") {
        println("Connected! Type a message and press Enter:")

        val receiveJob = launch {
            for (frame in incoming) {
                if (frame is Frame.Text) {
                    println(frame.readText())
                }
            }
        }

        val scanner = Scanner(System.`in`)
        while (scanner.hasNextLine()) {
            val message = scanner.nextLine()

            if (message.equals("/quit", ignoreCase = true)) {
                println("Disconnecting...")
                close(CloseReason(CloseReason.Codes.NORMAL, "Client exited"))
                break
            }

            send(Frame.Text(message))
        }

        receiveJob.cancel()
    }

    client.close()
}