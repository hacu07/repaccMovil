package com.example.repacc.perfilEdicion

import android.content.Context
import com.example.repacc.R
import com.example.repacc.perfilEdicion.event.DepartamentoEvent
import com.example.repacc.perfilEdicion.event.EdicionPerfilEvent
import com.example.repacc.perfilEdicion.event.MunicipioEvent
import com.example.repacc.perfilEdicion.event.PaisEvent
import com.example.repacc.perfilEdicion.model.PerfilEdicionModel
import com.example.repacc.perfilEdicion.model.PerfilEdicionModelClass
import com.example.repacc.perfilEdicion.view.PerfilEdicionActivity
import com.example.repacc.perfilEdicion.view.PerfilEdicionView
import com.example.repacc.pojo.Departamento
import com.example.repacc.pojo.Municipio
import com.example.repacc.pojo.Pais
import com.example.repacc.pojo.Usuario
import com.example.repacc.util.Constantes
import com.example.repacc.util.Util
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class PerfilEdicionPresenterClass : PerfilEdicionPresenter {

    private var mView:PerfilEdicionView? = null
    private var mModel:PerfilEdicionModel? = null
    private var usuarioModif:Usuario? = Constantes.config?.usuario

    private var paises:List<Pais>? = null
    private var departamentos:List<Departamento>? = null
    private var municipios:List<Municipio>? = null

    private val listaRh = listOf("Seleccione","A-","A+","O-","O+","B-","B+","AB-","AB+")

    constructor(perfilView: PerfilEdicionActivity){
        this.mView = perfilView
        this.mModel = PerfilEdicionModelClass()
    }

    override fun cargarDatos() {
        if(mView != null){
            Constantes.config?.usuario?.let {
                mView?.configSpiRH(listaRh)
                mView?.cargarDatos(it)

                mView?.cargarTipoSangre(listaRh.indexOf(it.tipoSangre))
            }
        }
    }

    override fun editarPerfil(context: Context) {
        if (mView != null){
            if (usuarioModif?.tipoSangre != null)
                usuarioModif?.let { mModel?.editarPerfil(context, it) }
            else
                mView?.mostrarMsj(context.getString(R.string.no_rh))
        }
    }

    override fun asignarRH(position: Int) {
        if (position != 0)
            usuarioModif?.tipoSangre = listaRh[position]
    }

    override fun asignarMunicipio(position: Int) {
            usuarioModif?.munNotif = municipios?.get(position)
    }

    override fun onCreate() {
        if(mView != null){
            EventBus.getDefault().register(this)
        }
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        mView = null
    }

    override fun obtenerPaises(context: Context) {
        mView.let {
            it?.mostrarProgreso(true)
            it?.habilitarCampos(false)
            mModel?.obtenerPaises(context)
        }
    }

    private fun obtenerIdPais(posicion: Int): String? {
        var idPais: String? = null
        idPais = paises?.get(posicion)?._id
        return idPais
    }

    private fun obtenerIdDpto(posicion: Int): String? {
        var idDpto: String? = null
        idDpto = departamentos?.get(posicion)?._id
        return idDpto
    }

    override fun obtenerMunicipios(context: Context, posicion: Int) {
        mView.let {
            var idDpto: String? = obtenerIdDpto(posicion)

            if (idDpto != null){
                mView?.habilitarCampos(false)
                mView?.mostrarProgreso(true)
                mModel?.obtenerCiudades(context, idDpto)
            }
        }
    }

    override fun obtenerDepartamentos(context: Context, posicion: Int) {
        mView.let {
            var idPais = obtenerIdPais(posicion);

            if (idPais != null){
                mView?.habilitarCampos(false)
                mView?.mostrarProgreso(true)
                mModel?.obtenerDepartamentos(context, idPais)
            }
        }
    }

    private fun obtenerNombresPaises(lista: List<Pais>): List<String> {
        val nombrePais= mutableListOf<String>()

        lista.forEach {
            nombrePais.add(it.nombre)
        }

        return nombrePais
    }

    private fun obtenerNombresDptos(lista: List<Departamento>): List<String> {
        val nombreDpto= mutableListOf<String>()
        lista.forEach {
            nombreDpto.add(it.nombre)
        }
        return nombreDpto
    }

    private fun obtenerNombresMuni(lista: List<Municipio>): List<String> {
        val nombreMuncp= mutableListOf<String>()
        lista.forEach {
            nombreMuncp.add(it.nombre)
        }
        return nombreMuncp
    }

    @Subscribe
    public fun onEventPaisListener(paisEvent: PaisEvent){
        if (mView != null){
            mView?.mostrarProgreso(false)
            mView?.habilitarCampos(true)

            when(paisEvent.typeEvent){
                Util.SUCCESS -> {
                    paises = paisEvent.content
                    paises?.let { mView?.cargarPaises(obtenerNombresPaises(it)) }
                }

                Util.ERROR_DATA,Util.ERROR_RESPONSE,Util.ERROR_CONEXION->
                        paisEvent.msj?.let { msj -> mView?.mostrarMsj(msj) }

            }
        }
    }

    @Subscribe
    public fun onEventDptoListener(dptoEvent: DepartamentoEvent){
        if(mView != null){
            mView?.mostrarProgreso(false)
            mView?.habilitarCampos(true)

            when(dptoEvent.typeEvent){
                Util.SUCCESS -> {
                    departamentos = dptoEvent.content
                    departamentos?.let { mView?.cargarDepartamentos(obtenerNombresDptos(it)) }
                }

                Util.ERROR_DATA,Util.ERROR_RESPONSE,Util.ERROR_CONEXION->
                    dptoEvent.msj?.let { msj -> mView?.mostrarMsj(msj) }

            }
        }
    }

    @Subscribe
    public fun onEventMunicipioListener(municipioEvent: MunicipioEvent){
        if(mView != null){
            mView?.mostrarProgreso(false)
            mView?.habilitarCampos(true)

            when(municipioEvent.typeEvent){
                Util.SUCCESS -> {
                    municipios = municipioEvent.content
                    municipios?.let { mView?.cargarMunicipios(obtenerNombresMuni(it)) }
                }

                Util.ERROR_DATA,Util.ERROR_RESPONSE,Util.ERROR_CONEXION->
                    municipioEvent.msj?.let { msj -> mView?.mostrarMsj(msj) }

            }
        }
    }

    @Subscribe
    public fun onEventEditarPerfil(event: EdicionPerfilEvent){
        if (mView != null){
            mView?.mostrarProgreso(false)
            mView?.habilitarCampos(true)

            when(event.typeEvent){
                Util.SUCCESS->{
                    Constantes.config?.usuario = event.content
                    mView?.getContext()?.let { Util.guardarConfig(it) }
                    event.msj?.let { mView?.mostrarMsj(it) }
                }
                Util.ERROR_DATA,Util.ERROR_RESPONSE,Util.ERROR_CONEXION->
                    event.msj?.let { mView?.mostrarMsj(it) }
            }
        }
    }
}