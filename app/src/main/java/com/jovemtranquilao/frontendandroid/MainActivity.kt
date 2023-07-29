package com.jovemtranquilao.frontendandroid

import android.app.ActionBar
import android.os.Bundle
import android.text.Editable
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
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
    private var tableLayout: TableLayout? = null
    private var chefe: Spinner? = null
    private var subordinado: Spinner? = null

    private var colaboradorAPIIntegration: ColaborarAPIIntegration = ColaborarAPIIntegration()

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

        colaboradorAPIIntegration.recuperarColaboradores(this);
    }

    private fun bindFields() {
        nome = findViewById(R.id.editTextText)
        senha = findViewById(R.id.editTextTextPassword)
        tableLayout = findViewById(R.id.tabela)
        chefe = findViewById(R.id.spinner)
        subordinado = findViewById(R.id.subordinado)
    }


    fun tratarCallback(response: ArrayList<ColaboradorDTO>) {
        preencherTabela(response)
        preencherCombos(response)
    }


    private fun preencherCombos(
        lista: ArrayList<ColaboradorDTO>
    ) {
        val users = lista.toTypedArray()

        val arrayadapter = ArrayAdapter(
            this@MainActivity,
            android.R.layout.simple_spinner_dropdown_item,
            users
        )
        chefe?.adapter = arrayadapter

        subordinado?.adapter = arrayadapter
    }


    private fun preencherTabela(lista: ArrayList<ColaboradorDTO>) {
        lista.forEach {
            val tableRow = TableRow(applicationContext);
            val textView = TextView(applicationContext)
            textView.text = (
                it.nome + " - " + it.score+ " - " + it.chefe?.nome
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

    fun salvarColaborador(view: View) {
        val toString = view.toString()
        val nomeValue = nome?.text
        val senhaValue = senha?.text

        colaboradorAPIIntegration.salvarColaborador(nomeValue, senhaValue, this)
    }

    fun associaChefe(view: View) {
        val toString = view.toString()
        val chefeId = (chefe?.selectedItem as ColaboradorDTO).id
        val subordinadoId = (subordinado?.selectedItem as ColaboradorDTO).id

        colaboradorAPIIntegration.associaChefe(chefeId, subordinadoId, this@MainActivity)
    }


    fun limparCampos() {
        nome?.text?.clear()
        senha?.text?.clear()
        colaboradorAPIIntegration.recuperarColaboradores(this@MainActivity)
    }

}