package com.example.repacc.perfilEdicion.model

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.repacc.perfilEdicion.event.DepartamentoEvent
import com.example.repacc.perfilEdicion.event.EdicionPerfilEvent
import com.example.repacc.perfilEdicion.event.MunicipioEvent
import com.example.repacc.perfilEdicion.event.PaisEvent
import com.example.repacc.perfilEdicion.model.DAO.*
import com.example.repacc.pojo.Usuario
import com.example.repacc.util.BasicCallback
import com.example.repacc.util.BasicEvent
import com.example.repacc.util.Util
import com.example.repacc.util.model.StorageUploadImageCallback
import org.greenrobot.eventbus.EventBus

class PerfilEdicionModelClass: PerfilEdicionModel, PaisCallback, DepartamentoCallback, MunicipioCallback {

    private var mDAO: PerfilEdicionDAO
    lateinit var mStorage: StorageProfile

    constructor(){
        mDAO = PerfilEdicionDAO()
        mStorage = StorageProfile()
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

    override fun editarPerfil(
        context: Context,
        usuario: Usuario,
        mPhotoSelectedUri: Uri?
    ) {
        mDAO.editarPerfil(context, usuario, object: EdicionPerfilCallback{
            override fun response(event: EdicionPerfilEvent) {

                // Si registró correctamente el perfil
                val response = event as EdicionPerfilEvent
                if (Util.SUCCESS == response.typeEvent && mPhotoSelectedUri != null){
                    // Almacena la imagen en storage de firebase
                    mStorage.uploadImageReport(
                        imageUri = mPhotoSelectedUri,
                        usuario = event.content!!,
                        callback = object: StorageUploadImageCallback {
                            override fun onSuccess(newUri: Uri) {
                                // registro exitoso en firebase storage
                                event.content!!.foto = newUri.toString()
                                mDAO.actualizarRutaImagen(
                                    context,
                                    event.content!!,
                                    object : BasicCallback {
                                        override fun response(eventImage: Any) {
                                            // Si actualizó la ruta de la imagen
                                            //val basicEvent = eventImage as BasicEvent
                                            postEditarPerfil(event)
                                        }
                                    }
                                )
                            }

                            override fun onError(resMsg: Int) {
                                // No se logró registrar en firebase storage
                                Log.i("REPACC","error to save {${resMsg.toString()}}")
                            }
                        }
                    )
                }

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
