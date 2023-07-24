package com.jovemtranquilao.frontendandroid

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import com.jovemtranquilao.frontendandroid.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private var nome: EditText? = null
    private var senha: EditText? = null


    private var tableLayout : TableLayout? = null

    private var textView : TextView? = null;


    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAnchorView(R.id.fab)
                    .setAction("Action", null).show()
        }

        nome = findViewById(R.id.editTextText)
        senha = findViewById(R.id.editTextTextPassword)

        tableLayout = findViewById(R.id.tabela)
        textView = findViewById(R.id.textView2)

        recuperarColaboradores();
    }

    private fun recuperarColaboradores() {

        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.3.104:8080")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        //3. Create an instance of the interface:
        val apiService = retrofit.create(ApiService::class.java)

        //5. Make the request:
        apiService.get().enqueue(
            object :  Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    Log.i("Response", response.body().toString());
                    //Toast.makeText()
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            Log.i("onSuccess", response.body().toString());
                            textView?.text = response.body().toString()
                            val jsonArray : JsonArray = JsonParser().parse(response.body().toString()) as JsonArray
                            jsonArray.forEach {
                                println(it)
                            }
                        } else {
                            Log.i(
                                "onEmptyResponse",
                                "Returned empty response"
                            );//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                        }
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            }
        )

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    fun sendMessage(view: View) {
        val toString = view.toString()
        val nomeValue = nome?.text
        val senhaValue = senha?.text

        getMyData(nomeValue, senhaValue)
    }

    private fun getMyData(nomeValue: Editable?, senhaValue: Editable?) {

        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.3.104:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        //3. Create an instance of the interface:
        val apiService = retrofit.create(ApiService::class.java)

        //4. Create the request body:
        val body = mapOf(
            "nome" to nomeValue.toString(),
            "senha" to senhaValue.toString()
        )

        //5. Make the request:

        apiService.postRequest(body).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                System.out.println(response.body())
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                System.out.println("öi")
            }
        })

    }

}