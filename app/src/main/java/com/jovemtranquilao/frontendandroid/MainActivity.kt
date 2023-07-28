package com.jovemtranquilao.frontendandroid

import android.os.Bundle
import android.text.Editable
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Spinner
import android.widget.TableLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.snackbar.Snackbar
import com.jovemtranquilao.frontendandroid.api.ColaborarAPIIntegration
import com.jovemtranquilao.frontendandroid.databinding.ActivityMainBinding
import com.jovemtranquilao.frontendandroid.dto.SpinnerDTO
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity(
) {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private var nome: EditText? = null
    private var senha: EditText? = null
    private var tableLayout : TableLayout? = null
    private var chefe: Spinner? = null
    private var subordinado: Spinner? = null

    private var colaboradorAPIIntegration : ColaborarAPIIntegration =  ColaborarAPIIntegration()

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        val onClickListener = binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAnchorView(R.id.fab)
                .setAction("Action", null).show()
        }

        bindFields()

        recuperarColaboradores();
    }

    private fun bindFields() {
        nome = findViewById(R.id.editTextText)
        senha = findViewById(R.id.editTextTextPassword)
        tableLayout = findViewById(R.id.tabela)
        chefe = findViewById(R.id.spinner)
        subordinado = findViewById(R.id.subordinado)
    }

    private fun recuperarColaboradores() {

        colaboradorAPIIntegration.recuperarColaboradores(tableLayout,chefe, subordinado, applicationContext,this@MainActivity )



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

    fun associaChefe(view: View) {
        val toString = view.toString()
        val chefeId = (chefe?.selectedItem as SpinnerDTO).id
        val subordinadoId = (subordinado?.selectedItem as SpinnerDTO).id

        associaChefe(chefeId, subordinadoId)
    }

    private fun getMyData(nomeValue: Editable?, senhaValue: Editable?) {

        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.2.2:8080")
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
                nome?.text?.clear()
                senha?.text?.clear()
                recuperarColaboradores();
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                System.out.println("öi")
            }
        })

    }

    private fun associaChefe(chefeId: Int, subordinadoId: Int) {

        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.2.2:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        //3. Create an instance of the interface:
        val apiService = retrofit.create(ApiService::class.java)

        //4. Create the request body:
        val body = mapOf(
            "idChefe" to chefeId,
            "idSubordinado" to subordinadoId
        )

        //5. Make the request:

        apiService.associaChefe(body).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                nome?.text?.clear()
                senha?.text?.clear()
                recuperarColaboradores();
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                System.out.println("öi")
            }
        })

    }

}