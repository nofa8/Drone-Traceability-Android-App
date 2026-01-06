package dev.epic.dronetraceability.data.remote

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import okhttp3.*

class DroneWebSocketClient(
    private val okHttpClient: OkHttpClient
) {

    private val _events = MutableSharedFlow<String>(extraBufferCapacity = 64)
    val events = _events.asSharedFlow()

    fun connect(url: String) {
        val request = Request.Builder().url(url).build()
        println("WS: Connecting to $url")

        okHttpClient.newWebSocket(
            request,
            object : WebSocketListener() {
                override fun onOpen(webSocket: WebSocket, response: Response) {
                    println("WS: Connected")
                }

                override fun onMessage(webSocket: WebSocket, text: String) {
                    println("WS: Received message: $text")
                    _events.tryEmit(text)
                }

                override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                    println("WS: Failure: ${t.message}")
                }
            }
        )
    }
}
