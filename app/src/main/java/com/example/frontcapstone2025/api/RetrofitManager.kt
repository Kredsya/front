package com.example.frontcapstone2025.api

import android.util.Log
import com.example.frontcapstone2025.BuildConfig
import com.google.gson.GsonBuilder
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit


class RetrofitManager {
    companion object {
        val instance: RetrofitManager by lazy { RetrofitManager() }
    }

    private val gson = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
        .create()

    val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS)     // 📌 백엔드가 오래 걸린다면 read timeout 늘리기
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()


    private val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(client)
        .build()
    private val apiService = retrofit.create(ApiService::class.java)

    suspend fun getWifiPosition(
        upDistance: Double,
        downDistance: Double,
        frontDistance: Double,
        leftDistance: Double,
        armLength: Double,
        onSuccess: (WifiPosition) -> Unit,
        onFailure: () -> Unit

    ) {
        try {
            val response = apiService.getWifiPosition(
                upDistance,
                downDistance,
                frontDistance,
                leftDistance,
                armLength

            )
            if (response.isSuccessful) {
                val wifiPosition = response.body()
                Log.d("getWifiPosition", "Success: ${wifiPosition.toString()}")
                if (wifiPosition != null) {
                    onSuccess(wifiPosition)
                }
            } else {
                Log.e("getWifiPosition", "Error: ${response.errorBody()}")
//                onFailure()
            }
        } catch (e: Exception) {
            Log.e("getWifiPosition", e.toString())
//            onFailure()
        }
    }

    suspend fun analyzePcap(
        file: File,
        onSuccess: (List<String>) -> Unit,
        onFailure: () -> Unit,
    ) {
        try {
            val requestBody = file.asRequestBody("application/octet-stream".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("file", file.name, requestBody)
            val response = apiService.analyzePcap(body)
            if (response.isSuccessful) {
                val result = response.body() ?: emptyList()
                Log.d("analyzePcap", "Success: $result")
                onSuccess(result[0])
            } else {
                Log.e("analyzePcap", "Error: ${response.errorBody()}")
                onFailure()
            }
        } catch (e: Exception) {
            Log.e("analyzePcap", e.toString())
            onFailure()
        }
    }
}