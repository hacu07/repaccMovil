package com.example.repacc.perfil.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.example.repacc.R
import com.example.repacc.perfil.PerfilPresenter
import com.example.repacc.perfil.PerfilPresenterClass
import com.example.repacc.perfilEdicion.view.PerfilEdicionActivity
import com.example.repacc.pojo.Usuario
import com.example.repacc.util.Util
import kotlinx.android.synthetic.main.activity_perfil.*
import kotlinx.android.synthetic.main.perfil_contenido.*

class PerfilActivity : AppCompatActivity(), PerfilView {

    private var mPresenter: PerfilPresenter? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)
        mPresenter = PerfilPresenterClass(this)
        mPresenter?.cargarDatos()

        configToolbar()
    }

    private fun configToolbar() {
        setSupportActionBar(toolbarPerfil)
    }

    /*
    * Opcion para editar perfil en toolbar
    * */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_perfil, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.mneditarPerfil -> irEditarPerfil()
        }
        return super.onOptionsItemSelected(item)
    }


    private fun irEditarPerfil() {
        val intent = Intent(this, PerfilEdicionActivity::class.java)
        startActivity(intent)
    }

    /***********************************************
            Perfil View
     */

    override fun cargarDatos(usuario: Usuario) {
        if(usuario != null){
            tvNombre.text = usuario.nombre?.trim()
            tvUsuario.text = usuario.usuario?.trim()
            tvRH.text = usuario.tipoSangre?.trim()
            etQrPerfil.setText(usuario.qr?.trim())
            etCorreoPerfil.setText(usuario.correo?.trim())
            etDireccionPerfil.setText(usuario.rol?.nombre)
            etCelularPerfil.setText(usuario.celular?.trim())

            // carga imagen de pefil
            if(usuario.foto != null){
                Glide.with(this)
                    .load(usuario.foto)
                    .error(R.drawable.user)
                    .centerCrop()
                    .into(imvFoto)
            }
        }
    }
}
