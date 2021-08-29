package com.gamesofni.memoriarty.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header

private const val BASE_URL =
    "https://memoriarty.herokuapp.com/api/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    // Using Moshi converter
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface MemoriartyApiService {
    @GET("today")
//    @Headers({"Cache-Control: max-age=640000"; "User-Agent: My-App-Name"})
    // suspend to be able to call it from within a coroutine
    suspend fun getRepeats(@Header("Cookie") cookie:  String): TodayResponseItem

}

// initialize the Retrofit service
object MemoriartyApi {
    val retrofitService : MemoriartyApiService by lazy {
        retrofit.create(MemoriartyApiService::class.java) }
}
