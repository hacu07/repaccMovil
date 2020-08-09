package com.example.repacc.perfilEdicion.view

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import com.bumptech.glide.Glide
import com.example.repacc.R
import com.example.repacc.perfilEdicion.PerfilEdicionPresenter
import com.example.repacc.perfilEdicion.PerfilEdicionPresenterClass
import com.example.repacc.pojo.*
import com.example.repacc.util.Constantes
import com.example.repacc.util.Util
import kotlinx.android.synthetic.main.activity_perfil_edicion.*
import kotlinx.android.synthetic.main.perfil_contenido_edicion.*

class PerfilEdicionActivity : AppCompatActivity(), PerfilEdicionView {

    private var mPresenter: PerfilEdicionPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_edicion)

        Util.getPreferences(this)
        configToolbar()
        mPresenter = PerfilEdicionPresenterClass(this)
        mPresenter?.onCreate()
        mPresenter?.obtenerPaises(this)
        mPresenter?.cargarDatos()
    }

    override fun configSpiRH(listaRH: List<String>) {
        spiRHEdicion.adapter = Util.obtenerArrayAdapter(listaRH, this)
        spiRHEdicion.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mPresenter?.asignarRH(position)
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    private fun configToolbar() {
        setSupportActionBar(toolbarPerfilEdicion)
    }

    override fun onDestroy() {
        Util.getPreferences(this)
        mPresenter?.onDestroy()
        super.onDestroy()
    }

    /*
    * Opcion para editar perfil en toolbar
    * */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_perfil_edicion, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.mnGuardarPerfil -> guardarPerfil()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun guardarPerfil() {
        mPresenter?.editarPerfil(this)
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }

    /**
     * PerfilEdicionView
     */
    override fun habilitarCampos(habilitar: Boolean) {

    }

    override fun mostrarProgreso(mostrar: Boolean) {

    }

    override fun mostrarMsj(msj: String) {
        Util.mostrarToast(this,msj)
    }

    override fun cargarPaises(paises: List<String>) {
        spiPaisNotif.adapter = Util.obtenerArrayAdapter(paises, this)

        spiPaisNotif.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mPresenter?.obtenerDepartamentos(this@PerfilEdicionActivity,position)
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    override fun cargarDepartamentos(departamentos: List<String>) {
        spiDepNotif.adapter = Util.obtenerArrayAdapter(departamentos, this)

        spiDepNotif.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    mPresenter?.obtenerMunicipios(this@PerfilEdicionActivity,position)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    override fun cargarMunicipios(municipios: List<String>) {
        spiMunNotif.adapter = Util.obtenerArrayAdapter(municipios, this)

        spiMunNotif.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mPresenter?.siCargoMunNotifUsuario(true)
                mPresenter?.asignarMunicipio(position)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    override fun cargarDatos(usuario: Usuario) {
        if(usuario != null){
            etNombreEdicion.setText(usuario.nombre?.trim())
            etUsuarioEdicion.setText(usuario.usuario?.trim())
            etCorreoEdicion.setText(usuario.correo?.trim())
            etCelularEdicion.setText(usuario.celular?.trim())
            swRecibNotif.isChecked = usuario.recibirNotif

            // carga imagen de pefil
            if(usuario.foto != null){
                Glide.with(this)
                    .load(usuario.foto)
                    .error(R.drawable.user)
                    .centerCrop()
                    .into(imvFotoEdicion)
            }
        }
    }

    override fun cargarTipoSangre(position: Int) {
        spiRHEdicion.setSelection(position,true)
    }

    override fun getContext(): Context{
        return this
    }
}
