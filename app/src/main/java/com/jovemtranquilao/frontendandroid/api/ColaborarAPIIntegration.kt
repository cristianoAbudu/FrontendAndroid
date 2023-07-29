package com.jovemtranquilao.frontendandroid.api

import android.R
import android.app.ActionBar
import android.content.Context
import android.text.Editable
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import com.jovemtranquilao.frontendandroid.ApiService
import com.jovemtranquilao.frontendandroid.ColaboradorDTO
import com.jovemtranquilao.frontendandroid.MainActivity
import com.jovemtranquilao.frontendandroid.dto.SpinnerDTO
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class ColaborarAPIIntegration {


    private fun buildApiService(): ApiService {
        return Retrofit.Builder()
            .baseUrl("http://192.168.2.2:8080")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ApiService::class.java)
    }


    fun recuperarColaboradores(
        mainActivity: MainActivity
    ) {
        buildApiService().get().enqueue(
            object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    try {
                        if (response.isSuccessful()) {
                            mainActivity.tratarCallback(processaJSON(response))
                        }
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    t.printStackTrace()
                }
            }
        )
    }


    private fun processaJSON(
        response: Response<String>
    ): ArrayList<ColaboradorDTO> {

        var lista = ArrayList<ColaboradorDTO>()

        val jsonArray: JsonArray =
            JsonParser().parse(response.body().toString()) as JsonArray

        jsonArray.forEach {
            var colaboradorChefe: ColaboradorDTO? = null

            val chefe = it.asJsonObject.get("chefe")
            if (!chefe.isJsonNull) {
                colaboradorChefe = ColaboradorDTO(
                    Integer.valueOf(chefe.asJsonObject.get("id").toString()),
                    chefe.asJsonObject.get("nome").toString(),
                    chefe.asJsonObject.get("senha").toString(),
                    chefe.asJsonObject.get("score").toString(),
                    null
                )
            }
            lista.add(
                ColaboradorDTO(
                    Integer.valueOf(it.asJsonObject?.get("id").toString()),
                    it.asJsonObject.get("nome").toString(),
                    it.asJsonObject.get("senha").toString(),
                    it.asJsonObject.get("score").toString(),
                    colaboradorChefe
                )
            )

        }
        return lista;
    }

    fun salvarColaborador(nomeValue: Editable?, senhaValue: Editable?, mainActivity: MainActivity) {

        val body = mapOf(
            "nome" to nomeValue.toString(),
            "senha" to senhaValue.toString()
        )

        buildApiService().postRequest(body).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                mainActivity.limparCampos()
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
               t.printStackTrace()
            }
        })

    }

    fun associaChefe(chefeId: Int, subordinadoId: Int, mainActivity: MainActivity) {
        val body = mapOf(
            "idChefe" to chefeId,
            "idSubordinado" to subordinadoId
        )

        buildApiService().associaChefe(body).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                mainActivity.limparCampos()
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                t.printStackTrace()
            }
        })

    }
}