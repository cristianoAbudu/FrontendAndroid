package com.jovemtranquilao.frontendandroid

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/")
    fun postRequest(@Body body: Map<String, String>): Call<ResponseBody>

}