package com.example.repacc.perfilEdicion.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import com.example.repacc.R
import com.example.repacc.perfilEdicion.PerfilEdicionPresenter
import com.example.repacc.perfilEdicion.PerfilEdicionPresenterClass
import com.example.repacc.pojo.Departamento
import com.example.repacc.pojo.Municipio
import com.example.repacc.pojo.Pais
import com.example.repacc.util.Util
import kotlinx.android.synthetic.main.activity_perfil_edicion.*
import kotlinx.android.synthetic.main.perfil_contenido_edicion.*

class PerfilEdicionActivity : AppCompatActivity(), PerfilEdicionView {

    private lateinit var mPresenter: PerfilEdicionPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_edicion)

        configToolbar()

        mPresenter = PerfilEdicionPresenterClass(this)
        mPresenter.onCreate()
        mPresenter.obtenerPaises(this)
    }

    private fun configToolbar() {
        setSupportActionBar(toolbarPerfilEdicion)
    }

    override fun onDestroy() {
        mPresenter.let {
            mPresenter.onDestroy()
        }
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
            R.id.mneditarPerfil -> guardarPerfil()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun guardarPerfil() {
        Util.mostrarToast(this,"GUARDAR PERFIL")
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }

    private fun obtenerArrayAdapter(lista: List<String>): SpinnerAdapter? {
        val dataAdapter:ArrayAdapter<String> = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item, lista)

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        return dataAdapter
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
        spiPaisNotif.adapter = obtenerArrayAdapter(paises)

        spiPaisNotif.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
    }

    override fun cargarDepartamentos(departamentos: List<String>) {

    }

    override fun cargarMunicipios(municipios: List<String>) {

    }
}
