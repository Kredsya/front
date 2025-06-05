package com.example.frontcapstone2025.api

import android.util.Log
import com.example.frontcapstone2025.BuildConfig
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime


class RetrofitManager {
    companion object {
        val instance: RetrofitManager by lazy { RetrofitManager() }
    }

    private val gson = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
        .create()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
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
}