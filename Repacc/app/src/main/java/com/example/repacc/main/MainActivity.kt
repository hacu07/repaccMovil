package com.example.repacc.main

import android.content.Intent
import com.example.repacc.registro.view.RegistroActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.repacc.R
import com.example.repacc.login.view.LoginActivity
import com.example.repacc.util.Util

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Util.URL_API = getString(R.string.url_api)
    }

    fun irRegistro(view: View) {
        val intent = Intent(this, RegistroActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun irLogin(view: View) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

}
