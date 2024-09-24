package com.nvcutu.voz

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nvcutu.voz.common.Resource
import com.nvcutu.voz.databinding.ActivityMainBinding
import com.nvcutu.voz.models.CookieModel
import com.nvcutu.voz.ui.home.LoginDialog
import com.nvcutu.voz.ui.home.LoginStep2Dialog
import org.jsoup.nodes.Document
import java.net.CookieManager


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        drawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)

        navView.setupWithNavController(navController)

        val imgView = navView.getHeaderView(0).findViewById<ImageView>(R.id.imgAccount)
        imgView.setOnClickListener {
            Current.fetch.getHtml(Resource.URL_BASE + "login/login") {
                when (it.result) {
                    null -> {
                        it.error?.let { err ->

                        }
                    }

                    else -> {
                        it.result?.let { doc ->
                            login(doc)
                        }
                    }
                }
            }
        }

        Current.setClient(this)
    }

    private fun login(doc: Document) {
        runOnUiThread {
            val loginDlg = LoginDialog(doc, this)
            loginDlg.show()
            loginDlg.findViewById<Button>(R.id.btnLogin).setOnClickListener {
                val userName =
                    loginDlg.findViewById<EditText>(R.id.edtUserName).text.toString()
                val password =
                    loginDlg.findViewById<EditText>(R.id.edtPassWord).text.toString()

                loginDlg.login(userName, password) { loginResult ->
                    when (loginResult.result) {
                        null -> {
                            loginResult.error?.let { loginError ->
                            }
                        }

                        else -> {
                            loginResult.result?.let { loginDoc ->
                                loginStep2(loginDoc)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun loginStep2(doc: Document) {
        runOnUiThread {
            val loginDlg = LoginStep2Dialog(doc, this)
            loginDlg.show()
            loginDlg.findViewById<Button>(R.id.btnLogin)
                .setOnClickListener {
                    val codeInput = loginDlg.findViewById<EditText>(
                        R.id.edtCodeStep2
                    ).text.toString()

                    loginDlg.loginStep2(codeInput) { loginStep2Result ->
                        when (loginStep2Result.result) {
                            false -> {

                            }

                            else -> {
                                val cookies = Current.cookieManager.cookieStore.cookies.map {
                                    val cookieModel = CookieModel()
                                    cookieModel.name = it.name
                                    cookieModel.value = it.value
                                    cookieModel
                                }

                                val cookieJson = Gson().toJson(cookies)
                                val sharedPref = this.getPreferences(MODE_PRIVATE)
                                with(sharedPref.edit()) {
                                    putString("cookie", cookieJson)
                                    apply()
                                }
                            }
                        }
                    }
                }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun openCloseNavigationDrawer(view: View) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }
}