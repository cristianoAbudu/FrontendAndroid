package com.jovemtranquilao.frontendandroid

import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("/")
    fun postRequest(@Body body: Map<String, String>): Call<ResponseBody>

    @GET("/")
    fun get(): Call<String>

    @POST("/associaChefe")
    fun associaChefe(@Body body: Map<String, Int>): Call<ResponseBody>


}