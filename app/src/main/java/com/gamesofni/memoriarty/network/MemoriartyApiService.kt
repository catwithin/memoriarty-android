package com.gamesofni.memoriarty.network

import com.gamesofni.memoriarty.Config
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*
import java.util.*


private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    // Using Moshi converter
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(Config().BASE_URL)
    .build()

interface MemoriartyApiService {
    @PUT("repeat")
    @Headers(
        "Content-Type: application/json",
        "Referer: https://memoriarty.herokuapp.com/"

    )
    suspend fun putRepeat(@Header("Cookie") cookie:  String, @Body body: RequestBody): ChunkJson


    @GET("today")
//    @Headers({"Cache-Control: max-age=640000"; "User-Agent: My-App-Name"})
    // suspend to be able to call it from within a coroutine
    suspend fun getRepeats(@Header("Cookie") cookie:  String): Response<TodayResponseItem>


    @POST("login")
    @Headers(
        "Content-Type: application/json",
        "Referer: https://memoriarty.herokuapp.com/"
    )
    suspend fun loginUser(@Body body: RequestBody): Response<LoginResponseJson>
}

// initialize the Retrofit service
object MemoriartyApi {
    val retrofitService : MemoriartyApiService by lazy {
        retrofit.create(MemoriartyApiService::class.java) }
}
