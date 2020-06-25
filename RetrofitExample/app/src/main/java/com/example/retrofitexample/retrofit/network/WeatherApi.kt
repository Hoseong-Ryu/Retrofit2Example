package com.example.retrofitexample.retrofit.network

import com.example.retrofitexample.retrofit.domain.WeatherDTO
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi{

    @GET("weather")
    fun getOkHttpWeather(
        @Query("q") city: String = "London",
        @Query("appid") appid: String
    ) : Call<WeatherDTO>

    @GET("weather")
    fun getRxWeather(
        @Query("q") city: String = "London",
        @Query("appid") appid: String
    ) : Observable<WeatherDTO>
}