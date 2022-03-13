package com.example.finalproject

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.*

interface CovidApi {
    @GET(".")
    fun getCovidInformation():  Call<Array<JsonObject>>
    @GET("{x}")
    fun getCountry(@Path("x") search : String,):  Call<JsonObject>
}