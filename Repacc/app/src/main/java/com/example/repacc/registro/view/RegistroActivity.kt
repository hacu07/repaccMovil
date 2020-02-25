package com.example.repacc.registro.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.repacc.R
import com.example.repacc.main.MainActivity
import com.example.repacc.pojo.Usuario
import com.example.repacc.util.Util
import java.io.Serializable

class RegistroActivity : AppCompatActivity(), RegistroView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun registrar(view: View) {}

    /*****************************************
     * Interface RegistroView
     *****************************************/
    override fun habilitarElementos() {

    }

    override fun inhabilitarElementos() {

    }

    override fun mostrarProgreso() {

    }

    override fun ocultarProgreso() {

    }

    override fun mostrarMsj(msj: String) {
        Util.mostrarToast(this, msj)
    }

    override fun irLogin(usuario: Usuario) {

    }
}
