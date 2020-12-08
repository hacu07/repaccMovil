package com.example.repacc.contacto.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.repacc.R
import com.example.repacc.contacto.ContactoPresenter
import com.example.repacc.contacto.ContactoPresenterClass
import com.example.repacc.contacto.view.adapter.ContactoAdapter
import com.example.repacc.contacto.view.adapter.OnItemSolicitudListener
import com.example.repacc.contacto.view.adapter.SolicitudAdapter
import com.example.repacc.contactoAgregar.view.ContactoAgregarActivity
import com.example.repacc.pojo.Contacto
import com.example.repacc.pojo.Solicitud
import com.example.repacc.pojo.Usuario
import com.example.repacc.util.Util
import kotlinx.android.synthetic.main.activity_contacto.*
import kotlinx.android.synthetic.main.toolbar.*

class ContactoActivity : AppCompatActivity(), ContactoView, OnItemSolicitudListener {

    private lateinit var mPresenter: ContactoPresenter
    private lateinit var mAdapterSolicitud : SolicitudAdapter
    private lateinit var mAdapterContacto: ContactoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacto)

        inicializar()
        mPresenter = ContactoPresenterClass(this)
        mPresenter.onCreate()
        mPresenter.cargarSolicitudes(this)
        mPresenter.cargarContactos(this)
    }

    override fun onDestroy() {
        mPresenter.onDestroy()
        super.onDestroy()
    }

    private fun inicializar() {
        rvSolicitud.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.HORIZONTAL,
            false)
        rvContacto.layoutManager = LinearLayoutManager(this)
    }


    /*
        Conctacto View
     */
    override fun habilitarElementos(siHabilita: Boolean) {
        rvSolicitud.isEnabled   = siHabilita
        rvContacto.isEnabled    = siHabilita
        fabContacto.isEnabled   = siHabilita
    }

    override fun mostrarProgreso(siMuestra: Boolean) {
        pbMenu.visibility = if(siMuestra) View.VISIBLE else View.GONE
    }

    override fun mostrarProgresoSolicitudes(siMuestra: Boolean) {
        pbSolicitudes.visibility = if(siMuestra) View.VISIBLE else View.GONE
    }

    override fun mostrarProgresoContactos(siMuestra: Boolean) {
        pbContactos.visibility = if(siMuestra) View.VISIBLE else View.GONE
    }

    override fun mostrarMsj(msj: String) {
        Util.mostrarToast(this,msj)
    }

    override fun mostrarMsjSolicitudes(msj: String) {
        rvSolicitud.visibility = View.GONE

        tvErrorSolicitud.visibility = View.VISIBLE
        tvErrorSolicitud.setText(msj)
    }

    override fun mostrarMsjContactos(msj: String) {
        rvContacto.visibility = View.GONE

        tvErrorContactos.visibility = View.VISIBLE
        tvErrorContactos.setText(msj)
    }

    override fun cargarSolicitudes(solicitudes: ArrayList<Solicitud>?) {
        tvErrorSolicitud.visibility = View.GONE

        rvSolicitud.visibility = View.VISIBLE
        mAdapterSolicitud = SolicitudAdapter(solicitudes,this, this)
        rvSolicitud.adapter = mAdapterSolicitud
    }

    override fun cargarContactos(contactos: ArrayList<Contacto>?) {
        tvErrorContactos.visibility = View.GONE

        rvContacto.visibility = View.VISIBLE
        val contacts = if(contactos != null) contactos else ArrayList<Contacto>()
        mAdapterContacto = ContactoAdapter(contacts,this)
        rvContacto.adapter = mAdapterContacto
    }

    // Si acepto o rechazo la solicitud
    override fun listenerEstadoSolicitud(solicitud: Solicitud, aceptado: Boolean) {
        mPresenter.cambiarEstadoSolicitud(this, solicitud, aceptado)
    }


    /*
    * El contacto fue aceptado por el usuario
    * Se elimina de recycler de Solicitud y se agrega al de contacto
    * */
    override fun agregarContacto(contacto: Contacto?) {
        mAdapterSolicitud.eliminarSolicitud(contacto)
        mAdapterContacto.agregarContacto(contacto)

        if(mAdapterSolicitud.solicitudes?.size == 0){
            rvSolicitud.visibility = View.GONE
            tvErrorSolicitud.visibility = View.VISIBLE
        }
    }

    override fun eliminarSolicitudRechazada(solicitud: Solicitud) {
        mAdapterSolicitud.eliminarSolicitud(solicitud)
    }

    /* Inicia actividad para buscar contacto */
    fun buscarContacto(view: View) {
        val intent = Intent(this, ContactoAgregarActivity::class.java)
        startActivity(intent)
    }
}
