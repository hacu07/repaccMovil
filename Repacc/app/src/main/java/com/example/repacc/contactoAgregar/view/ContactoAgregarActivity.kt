package com.example.repacc.contactoAgregar.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.repacc.R
import com.example.repacc.contactoAgregar.ContactoAgregarPresenter
import com.example.repacc.contactoAgregar.ContactoAgregarPresenterClass
import com.example.repacc.contactoAgregar.view.adapter.ContactoAgregarAdapter
import com.example.repacc.contactoAgregar.view.adapter.ContactoAgregarClickListener
import com.example.repacc.pojo.Usuario
import com.example.repacc.util.Util
import kotlinx.android.synthetic.main.activity_contacto_agregar.*
import java.util.ArrayList

class ContactoAgregarActivity : AppCompatActivity(), SearchView.OnQueryTextListener,
    ContactoAgregarView, ContactoAgregarClickListener {

    lateinit var mPresenter: ContactoAgregarPresenter
    private lateinit var mAdapter: ContactoAgregarAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacto_agregar)

        inicializar()

        mPresenter = ContactoAgregarPresenterClass(this)
        mPresenter.onCreate()
    }

    private fun inicializar() {
        svContacto.setOnQueryTextListener(this)
        rvSolicitudEnviar.layoutManager = LinearLayoutManager(this)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null){
            if(query.length >= 4){
                buscarUsuario(query.trim())
            }else{
                mostrarMsj(getString(R.string.minimo_4_caracteres))
            }
        }
        return false
    }

    private fun buscarUsuario(query: String) {
        mPresenter.buscarContacto(this, query)
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return false
    }

    override fun onDestroy() {
        mPresenter.onDestroy()
        super.onDestroy()
    }

    override fun habilitarElementos(habilita: Boolean) {
        svContacto.isEnabled = habilita
        rvSolicitudEnviar.isEnabled = habilita
    }

    override fun mostrarProgreso(mostrar: Boolean) {
        pbContactoAgregar.visibility = if (mostrar) View.VISIBLE else View.GONE
    }

    override fun mostrarMsj(msj: String) {
        Util.mostrarToast(this,msj)
    }

    override fun cargarUsuarios(usuarios: ArrayList<Usuario>) {
        mAdapter = ContactoAgregarAdapter(usuarios, this, this)
        rvSolicitudEnviar.adapter = mAdapter
    }

    override fun limpiar() {
        finish()
    }

    // Clic sobre el boton agregar contacto
    override fun onContactoClickListener(usuario: Usuario) {
        mPresenter?.registrarSolicitud(this,usuario)
    }
}
