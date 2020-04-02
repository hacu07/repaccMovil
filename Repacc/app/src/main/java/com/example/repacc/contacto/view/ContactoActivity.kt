package com.example.repacc.contacto.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.repacc.R
import com.example.repacc.contacto.ContactoPresenter
import com.example.repacc.contacto.ContactoPresenterClass
import com.example.repacc.contacto.view.adapter.ContactoAdapter
import com.example.repacc.contacto.view.adapter.OnItemSolicitudListener
import com.example.repacc.contacto.view.adapter.SolicitudAdapter
import com.example.repacc.pojo.Contacto
import com.example.repacc.pojo.Solicitud
import com.example.repacc.pojo.Usuario
import kotlinx.android.synthetic.main.activity_contacto.*

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

    }

    override fun mostrarProgreso(siMuestra: Boolean) {

    }

    override fun mostrarMsj(msj: String) {

    }

    override fun cargarSolicitudes(solicitudes: ArrayList<Solicitud>?) {
        mAdapterSolicitud = SolicitudAdapter(solicitudes,this, this)
        rvSolicitud.adapter = mAdapterSolicitud

    }

    override fun cargarContactos(contactos: ArrayList<Contacto>?) {
        val contacts = if(contactos != null) contactos else ArrayList<Contacto>()
        mAdapterContacto = ContactoAdapter(contacts,this)
        rvContacto.adapter = mAdapterContacto

    }

    // Si acepto o rechazo la solicitud
    override fun listenerEstadoSolicitud(solicitud: Solicitud, aceptado: Boolean) {
        mPresenter.cambiarEstadoSolicitud(this, solicitud._id, aceptado)
    }


    /*
    * El contacto fue aceptado por el usuario
    * Se elimina de recycler de Solicitud y se agrega al de contacto
    * */
    override fun agregarContacto(contacto: Contacto?) {
        mAdapterSolicitud.eliminarSolicitud(contacto)
        mAdapterContacto.agregarContacto(contacto)
    }
}
