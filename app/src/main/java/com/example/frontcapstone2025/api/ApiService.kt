package com.example.frontcapstone2025.api

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {

    @GET("/sambyeon/get_positiion")
    suspend fun getWifiPosition(
        @Query("up_distance") upDistance: Double,
        @Query("down_distance") downDistance: Double,
        @Query("front_distance") frontDistance: Double,
        @Query("left_distance") leftDistance: Double,
        @Query("arm_length") armLength: Double,
    ): Response<WifiPosition>

    @Multipart
    @POST("/pcap/analyze")
    suspend fun analyzePcap(
        @Part file: MultipartBody.Part,
    ): Response<List<List<String>>>
}