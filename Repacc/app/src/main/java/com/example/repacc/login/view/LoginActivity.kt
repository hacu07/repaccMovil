package com.example.repacc.login.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.repacc.R
import com.example.repacc.login.LoginPresenter
import com.example.repacc.login.LoginPresenterClass
import com.example.repacc.main.MainActivity
import com.example.repacc.menu.view.MenuActivity
import com.example.repacc.pojo.Usuario
import com.example.repacc.util.Util
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), LoginView {

    private var mPresenter:LoginPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mPresenter = LoginPresenterClass(this)
        mPresenter?.onCreate()
    }

    override fun onDestroy() {
        mPresenter?.onDestroy()
        super.onDestroy()
    }

    fun login(view: View) {
        mPresenter?.validarUsuario(
            this,
            Usuario(
                usuario = etUsuario.text.toString().trim(),
                contrasena = etContrasena.text.toString().trim()
            )
        )
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
        super.onBackPressed()
    }

    /*******************************************************
     * LOGIN VIEW
     */
    override fun habilitarElementos() {

    }

    override fun inhabilitarElementos() {

    }

    override fun mostrarDialogo() {

    }

    override fun ocultarDialogo() {

    }

    override fun mostrarMsj(msj: String) {
        Util.mostrarToast(this,msj)
    }

    override fun irInicio() {
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
        finish()
    }
}
