package dev.epic.dronetraceability.ui.pov

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DronePovViewModel : ViewModel() {

    // URLs de stream
    private val mainUrl = "https://demo.unified-streaming.com/k8s/features/stable/video/tears-of-steel/tears-of-steel.ism/.m3u8"
    private val fallbackUrl = "https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8"


    private val _streamUrl = MutableStateFlow(mainUrl)
    val streamUrl: StateFlow<String> = _streamUrl


    private var triedFallback = false

    fun handlePlaybackError() {
        if (!triedFallback) {
            _streamUrl.value = fallbackUrl
            triedFallback = true
        }
    }

    // Reseta o player para o mainUrl (ex: botão refresh)
    fun resetStream() {
        _streamUrl.value = mainUrl
        triedFallback = false
    }
}
