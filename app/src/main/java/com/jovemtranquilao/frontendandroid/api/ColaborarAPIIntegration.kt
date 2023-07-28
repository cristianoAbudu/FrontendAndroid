package com.jovemtranquilao.frontendandroid.api

import android.R
import android.app.ActionBar
import android.content.Context
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import com.jovemtranquilao.frontendandroid.ApiService
import com.jovemtranquilao.frontendandroid.MainActivity
import com.jovemtranquilao.frontendandroid.dto.SpinnerDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class ColaborarAPIIntegration {

    private fun buildApiService(): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.2.2:8080")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ApiService::class.java)
    }

    fun recuperarColaboradores(
        tableLayout: TableLayout?,
        chefe: Spinner?,
        subordinado: Spinner?,
        applicationContext: Context,
        mainActivity: MainActivity
    ) {
       buildApiService().get().enqueue(
            object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                colocarDadosNaTela(
                                    response,
                                    tableLayout,
                                    applicationContext,
                                    mainActivity,
                                    chefe,
                                    subordinado
                                )

                            }
                        }
                    }catch (e : java.lang.Exception){
                        e.printStackTrace()
                    }
                }
                override fun onFailure(call: Call<String>, t: Throwable) {
                    t.printStackTrace()
                }
            }
        )

    }

    private fun colocarDadosNaTela(
        response: Response<String>,
        tableLayout: TableLayout?,
        applicationContext: Context,
        mainActivity: MainActivity,
        chefe: Spinner?,
        subordinado: Spinner?
    ) {
        val jsonArray: JsonArray =
            JsonParser().parse(response.body().toString()) as JsonArray

        tableLayout?.removeAllViews()
        var lista = ArrayList<SpinnerDTO>();

        jsonArray.forEach {
            lista.add(
                SpinnerDTO(
                    Integer.valueOf(it.asJsonObject?.get("id").toString()),
                    it.asJsonObject.get("nome").toString()
                )
            )

            val tableRow = TableRow(applicationContext);
            val textView = TextView(applicationContext)
            textView.text = (
                    it.asJsonObject.get("nome").toString()
                            + " - " + it.asJsonObject.get("score")
                        .toString()
                            + " - " + it.asJsonObject.get("chefe")
                        .toString()
                    )
            val linearLayout = LinearLayout(applicationContext)
            val params: LinearLayout.LayoutParams =
                LinearLayout.LayoutParams(
                    ActionBar.LayoutParams.MATCH_PARENT,
                    ActionBar.LayoutParams.WRAP_CONTENT
                )
            params.setMargins(5, 2, 2, 2)
            linearLayout.addView(textView, params)
            tableRow.addView(linearLayout) // Adding textView to tablerow.

            tableLayout?.addView(tableRow)
        }

        val users = lista.toTypedArray()

        val arrayadapter = ArrayAdapter(
            mainActivity,
            R.layout.simple_spinner_dropdown_item,
            users
        )
        chefe?.adapter = arrayadapter

        val arrayadapter2 = ArrayAdapter(
            mainActivity,
            R.layout.simple_spinner_dropdown_item,
            users
        )
        subordinado?.adapter = arrayadapter2
    }


}