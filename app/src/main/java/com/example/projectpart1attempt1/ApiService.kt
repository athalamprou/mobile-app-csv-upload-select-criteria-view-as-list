package com.example.projectpart1attempt1

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

//Interface sto opoio orizontai API endpoints gia epikoinwnia me server.js
//To Retrofit to xrhsimopoiei gia HTTP requests
interface ApiService {
    // POST request gia na anevasei lista me schedule entries sto server
    @POST("insert") //Endpoint gia eisagwgh schedule entries
    fun uploadSchedule(@Body schedule: List<ScheduleEntry>): Call<Void>

    @GET("/schedule") //GET request gia na anakthsei ta filtrarismena schedule entries ap to server
    fun getSchedule(
        @Query("columns") columns: String, //query parametros gia columns
        @Query("filters") filters: String //query parametros gia filters
    ): Call<List<ScheduleEntry>>
}