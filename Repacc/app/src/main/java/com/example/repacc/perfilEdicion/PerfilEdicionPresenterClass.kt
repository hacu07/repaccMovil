package com.example.repacc.perfilEdicion

import android.content.Context
import com.example.repacc.perfilEdicion.event.DepartamentoEvent
import com.example.repacc.perfilEdicion.event.MunicipioEvent
import com.example.repacc.perfilEdicion.event.PaisEvent
import com.example.repacc.perfilEdicion.model.PerfilEdicionModel
import com.example.repacc.perfilEdicion.model.PerfilEdicionModelClass
import com.example.repacc.perfilEdicion.view.PerfilEdicionActivity
import com.example.repacc.perfilEdicion.view.PerfilEdicionView
import com.example.repacc.pojo.Departamento
import com.example.repacc.pojo.Municipio
import com.example.repacc.pojo.Pais
import com.example.repacc.util.Util
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class PerfilEdicionPresenterClass : PerfilEdicionPresenter {

    private var mView:PerfilEdicionView? = null
    private var mModel:PerfilEdicionModel? = null

    private var paises:List<Pais>? = null
    private var departamentos:List<Departamento>? = null
    private var municipios:List<Municipio>? = null

    constructor(perfilView: PerfilEdicionActivity){
        this.mView = perfilView
        this.mModel = PerfilEdicionModelClass()
    }

    private fun obtenerNombresPaises(lista: List<Pais>): List<String> {
        val nombrePais= mutableListOf<String>("SELECCIONE PAIS")

        lista.forEach {
            nombrePais.add(it.nombre)
        }

        return nombrePais
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

    override fun obtenerDepartamentos(context: Context, idPais: String) {
        mView.let {
            mModel?.obtenerDepartamentos(context, idPais)
        }
    }

    override fun obtenerMunicipios(context: Context, idDpto: String) {
        mView.let {
            mModel?.obtenerCiudades(context, idDpto)
        }
    }

    @Subscribe
    private fun onEventPaisListener(paisEvent: PaisEvent){
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
    private fun onEventDptoListener(dptoEvent: DepartamentoEvent){
        if(mView != null){
            mView?.mostrarProgreso(false)
            mView?.habilitarCampos(true)

            when(dptoEvent.typeEvent){
                Util.SUCCESS -> {
                    departamentos = dptoEvent.content
                    //departamentos?.let { mView?.cargarDepartamentos(it) }
                }

                Util.ERROR_DATA,Util.ERROR_RESPONSE,Util.ERROR_CONEXION->
                    dptoEvent.msj?.let { msj -> mView?.mostrarMsj(msj) }

            }
        }
    }

    @Subscribe
    private fun onEventMunicipioListener(municipioEvent: MunicipioEvent){
        if(mView != null){
            mView?.mostrarProgreso(false)
            mView?.habilitarCampos(true)

            when(municipioEvent.typeEvent){
                Util.SUCCESS -> {
                    municipios = municipioEvent.content
                    //municipios?.let { mView?.cargarMunicipios(obtenerNombresPaises(it)) }
                }

                Util.ERROR_DATA,Util.ERROR_RESPONSE,Util.ERROR_CONEXION->
                    municipioEvent.msj?.let { msj -> mView?.mostrarMsj(msj) }

            }
        }
    }



}