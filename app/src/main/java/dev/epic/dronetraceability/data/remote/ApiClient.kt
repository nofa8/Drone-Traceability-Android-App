package dev.epic.dronetraceability.data.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dev.epic.dronetraceability.data.remote.DroneApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

@OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
object ApiClient {

    private const val BASE_URL = "http://10.0.2.2:5101/"

    private val json = Json {
        ignoreUnknownKeys = true   // campos extra do backend não quebram a app
        isLenient = true
    }

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(
            json.asConverterFactory("application/json".toMediaType())
        )
        .build()

    val api: DroneApi = retrofit.create(DroneApi::class.java)
}
