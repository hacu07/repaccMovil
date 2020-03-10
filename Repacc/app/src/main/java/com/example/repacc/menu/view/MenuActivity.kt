package com.example.repacc.menu.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.repacc.R
import com.example.repacc.perfil.view.PerfilActivity
import kotlinx.android.synthetic.main.activity_menu.*

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        setSupportActionBar(toolbarMenu)
    }


    fun irPerfil(view: View) {
            val intent = Intent(this, PerfilActivity::class.java)
        startActivity(intent)
    }


}
