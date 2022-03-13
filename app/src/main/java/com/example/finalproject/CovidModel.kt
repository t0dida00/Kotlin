package com.example.finalproject

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class CovidModel :ViewModel(){

    val covidInformation = mutableStateOf(listOf<JsonObject>())
      val temp= mutableListOf<JsonObject>()

    var flag = mutableStateOf("")
    var cases = mutableStateOf("")
    var deaths = mutableStateOf("")
    var country = mutableStateOf("")
    var recovered = mutableStateOf("")


    val api: CovidApi by lazy {
             Retrofit.Builder()
                 .baseUrl("https://corona.lmao.ninja/v2/countries/")
                 .addConverterFactory(GsonConverterFactory.create())
                 .build()
                 .create()
         }

    init{
        viewModelScope.launch {
            val resp = api.getCovidInformation().awaitResponse();
        if(resp.isSuccessful)
        {
            val data  = resp.body()
            var i = 0
            for (d in data!!)
            {
           // Log.d("--------------" , d["country"]["flag"].toString())
                if(i < 20)
                {
                    temp.add(d);
                    i++
                }
                else{
                    break;
                }


            }

            covidInformation.value = temp


        }
            else{
            Log.d("--------------" , "False")
        }

        }
    }

    fun getCountry(search: String){
        viewModelScope.launch {
            val response = api.getCountry(search).awaitResponse()

            if(response.isSuccessful){
                val data  = response.body()
                val temp =  JSONObject(data!!["countryInfo"].toString())
                country.value = data!!["country"].toString()
                cases.value = data!!["cases"].toString()
                deaths.value = data!!["deaths"].toString()
                recovered.value = data!!["recovered"].toString()
                flag.value = temp["flag"].toString()

            } else {
                Log.d("***", "Not success")
            }
        }
    }
}