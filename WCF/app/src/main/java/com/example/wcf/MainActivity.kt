package com.example.wcf

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import com.example.wcf.Util.Util
import com.example.wcf.cliente.vista.ClienteActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Util.URL_API = getString(R.string.url_api)
    }

    fun irClientes(view: View) {
        val intent = Intent(this,ClienteActivity::class.java)
        startActivity(intent)
    }
}
