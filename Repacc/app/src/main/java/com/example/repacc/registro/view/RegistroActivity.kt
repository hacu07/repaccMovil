package com.example.repacc.registro.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.repacc.R
import com.example.repacc.main.MainActivity
import com.example.repacc.pojo.Usuario
import com.example.repacc.registro.RegistroPresenter
import com.example.repacc.registro.RegistroPresenterClass
import com.example.repacc.util.Util
import kotlinx.android.synthetic.main.activity_registro.*

class RegistroActivity : AppCompatActivity(), RegistroView {

    private lateinit var mPresenter:RegistroPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        mPresenter = RegistroPresenterClass(this)
        mPresenter.onCreate()
    }

    override fun onDestroy() {
        mPresenter.onDestroy()
        super.onDestroy()
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
        super.onBackPressed()
    }

    fun registrar(view: View) {
        // Usuario a registrar
        var usuario = Usuario(
            usuario= etUsuario.text.toString().trim(),
            correo = etCorreo.text.toString().trim(),
            contrasena = etContrasena.text.toString().trim(),
            celular = etCelular.text.toString().trim(),
            nombre = etNombre.text.toString().trim()
        )

        mPresenter.registrarUsuario(usuario)

    }

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
