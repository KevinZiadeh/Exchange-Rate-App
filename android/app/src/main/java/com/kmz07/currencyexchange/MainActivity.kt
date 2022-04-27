package com.kmz07.currencyexchange

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.kmz07.currencyexchange.api.Authentication
import com.kmz07.currencyexchange.api.ExchangeService
import com.kmz07.currencyexchange.api.model.Transaction
import com.kmz07.currencyexchange.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var fab: FloatingActionButton? = null
    private var transactionDialog: View? = null
    private var menu: Menu? = null
    private var navView: NavigationView? = null
    private var navController: NavController? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Authentication.initialize(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarRealMain.toolbar)

        fab = binding.appBarRealMain.fab

        fab!!.setOnClickListener {
            showDialog()
        }

        val drawerLayout: DrawerLayout = binding.drawerLayout
        navView = binding.navView
        navController = findNavController(R.id.nav_host_fragment_content_real_main)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_exchange, R.id.nav_calculator, R.id.nav_statistics, R.id.nav_login,
                R.id.nav_register, R.id.nav_transactions, R.id.nav_exchangeUser, R.id.nav_logout
            ), drawerLayout
        )

        setupActionBarWithNavController(navController!!, appBarConfiguration)
        navView!!.setupWithNavController(navController!!)

    }

    private fun setMenu() {
        menu?.clear()
        if(Authentication.getToken() == null){
            navView!!.inflateMenu(R.menu.activity_main_drawer_logged_out)
        } else{
            navView!!.inflateMenu(R.menu.activity_main_drawer_logged_in)
        }

        val drawerLayout: DrawerLayout = binding.drawerLayout
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_exchange, R.id.nav_calculator, R.id.nav_statistics, R.id.nav_login,
                R.id.nav_register, R.id.nav_transactions, R.id.nav_exchangeUser, R.id.nav_logout
            ), drawerLayout
        )
        navController?.let { setupActionBarWithNavController(it, appBarConfiguration) }
        navController?.let { navView!!.setupWithNavController(it) }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        this.menu = navView?.menu
        setMenu()
        if(Authentication.getToken() != null) {
            navView!!.menu[5].setOnMenuItemClickListener(MenuItem.OnMenuItemClickListener {
                Authentication.clearToken()
                onCompleted()
                true
            })
        }
        return true
    }

    private fun onCompleted() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_real_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun showDialog() {
        transactionDialog = LayoutInflater.from(this)
            .inflate(R.layout.dialog_transaction, null, false)
        MaterialAlertDialogBuilder(this).setView(transactionDialog)
            .setTitle("Add Transaction")
            .setMessage("Enter transaction details")
            .setPositiveButton("Add") { dialog, _ ->
                val usdAmount =
                    transactionDialog?.findViewById<TextInputLayout>(R.id.txtInputUsdAmount)?.editText?.text
                        .toString();
                val lbpAmount =
                    transactionDialog?.findViewById<TextInputLayout>(R.id.txtInputLbpAmount)?.editText?.text
                        .toString();

                val usdToLbp = transactionDialog?.findViewById<RadioButton>(R.id.rdBtnSellUsd)
                    ?.isChecked() == true
                val lbpToUbp = transactionDialog?.findViewById<RadioButton>(R.id.rdBtnBuyUsd)
                    ?.isChecked() == true

                try {
                    val transaction = Transaction();
                    transaction.lbpAmount = lbpAmount.toFloat();
                    transaction.usdAmount = usdAmount.toFloat();
                    if (!usdToLbp && !lbpToUbp) {
                        throw Exception();
                    }
                    transaction.usdToLbp = usdToLbp;
                    addTransaction(transaction);
                    dialog.dismiss()
                } catch (e: Exception) {
                    Snackbar.make(
                        fab as View, "Could not add transaction.",
                        Snackbar.LENGTH_LONG
                    )
                        .show()
                }

            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun addTransaction(transaction: Transaction) {
        ExchangeService.exchangeApi().addTransaction(
            transaction,
            if (Authentication.getToken() != null) "Bearer ${Authentication.getToken()}" else null
        ).enqueue(object :
            Callback<Any> {
            override fun onResponse(
                call: Call<Any>, response:
                Response<Any>
            ) {
                Snackbar.make(
                    fab as View, "Transaction added!",
                    Snackbar.LENGTH_LONG
                )
                    .show()
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                Snackbar.make(
                    fab as View, "Could not add transaction.",
                    Snackbar.LENGTH_LONG
                )
                    .show()
            }
        })
    }

}