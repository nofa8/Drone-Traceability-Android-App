package dev.epic.dronetraceability.data.remote

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import okhttp3.*

class DroneWebSocketClient(
    private val okHttpClient: OkHttpClient
) {

    private val _events = MutableSharedFlow<String>(extraBufferCapacity = 64)
    val events = _events.asSharedFlow()

    private var webSocket: WebSocket? = null

    fun connect(url: String) {
        val request = Request.Builder().url(url).build()
        println("WS: Connecting to $url")

        webSocket = okHttpClient.newWebSocket(
            request,
            object : WebSocketListener() {
                override fun onOpen(ws: WebSocket, response: Response) {
                    println("WS: Connected")
                }

                override fun onMessage(ws: WebSocket, text: String) {
                    println("WS: Received message: $text")
                    _events.tryEmit(text)
                }

                override fun onFailure(ws: WebSocket, t: Throwable, response: Response?) {
                    println("WS: Failure: ${t.message}")
                }
            }
        )
    }

    fun send(message: String) {
        webSocket?.let {
            it.send(message)
            println("WS: sent message: $message")
        } ?: println("WS: Cannot send, not connected")
    }
}

