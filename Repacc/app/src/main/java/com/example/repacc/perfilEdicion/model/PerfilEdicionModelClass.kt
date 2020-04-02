package com.example.repacc.perfilEdicion.model

import android.content.Context
import com.example.repacc.perfilEdicion.event.DepartamentoEvent
import com.example.repacc.perfilEdicion.event.EdicionPerfilEvent
import com.example.repacc.perfilEdicion.event.MunicipioEvent
import com.example.repacc.perfilEdicion.event.PaisEvent
import com.example.repacc.perfilEdicion.model.DAO.*
import com.example.repacc.pojo.Usuario
import org.greenrobot.eventbus.EventBus

class PerfilEdicionModelClass: PerfilEdicionModel, PaisCallback, DepartamentoCallback, MunicipioCallback {

    private var mDAO: PerfilEdicionDAO

    constructor(){
        mDAO = PerfilEdicionDAO()
    }

    override fun obtenerPaises(context: Context) {
        mDAO.obtenerPaises(context,this)
    }

    override fun obtenerDepartamentos(context: Context, idPais: String) {
        mDAO.obtenerDepartamentos(context, idPais, this)
    }

    override fun obtenerCiudades(context: Context, idDpto: String) {
        mDAO.obtenerMunicipios(context, idDpto, this)
    }

    override fun editarPerfil(context: Context, usuario: Usuario) {
        mDAO.editarPerfil(context, usuario, object: EdicionPerfilCallback{
            override fun response(event: EdicionPerfilEvent) {
                postEditarPerfil(event)
            }
        })
    }

    private fun postEditarPerfil(event: EdicionPerfilEvent){
        EventBus.getDefault().post(event)
    }

    /**
     *  PAIS CALLBACK
     */
    override fun response(paisEvent: PaisEvent) {
        postPais(paisEvent)
    }


    private fun postPais(paisEvent: PaisEvent) {
        EventBus.getDefault().post(paisEvent)
    }
    /**
     *  DEPARTAMENTO CALLBACK
     */
    override fun response(departamentoEvent: DepartamentoEvent) {
        postDepartamento(departamentoEvent)
    }

    private fun postDepartamento(departamentoEvent: DepartamentoEvent) {
        EventBus.getDefault().post(departamentoEvent)
    }

    /**
     *  MUNICIPIO CALLBACK
     */
    override fun response(municipioEvent: MunicipioEvent) {
        postMunicipio(municipioEvent)
    }

    private fun postMunicipio(municipioEvent: MunicipioEvent) {
        EventBus.getDefault().post(municipioEvent)
    }

    /**
    *   EDITAR PERFIL CALLBACK
    * */

}
