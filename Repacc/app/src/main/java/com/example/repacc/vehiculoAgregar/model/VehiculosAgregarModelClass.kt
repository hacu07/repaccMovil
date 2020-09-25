package com.example.repacc.vehiculoAgregar.model

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.repacc.pojo.Vehiculo
import com.example.repacc.util.BasicCallback
import com.example.repacc.util.BasicEvent
import com.example.repacc.util.Util
import com.example.repacc.util.model.StorageUploadImageCallback
import com.example.repacc.vehiculoAgregar.events.MarcaEvent
import com.example.repacc.vehiculoAgregar.events.ModeloEvent
import com.example.repacc.vehiculoAgregar.events.TipoEvent
import com.example.repacc.vehiculoAgregar.events.VehiculoEvent
import com.example.repacc.vehiculoAgregar.model.DAO.DAO
import com.example.repacc.vehiculoAgregar.model.DAO.StorageVehicle
import org.greenrobot.eventbus.EventBus

class VehiculosAgregarModelClass : VehiculoAgregarModel {

    lateinit var mDAO : DAO
    lateinit var mStorage: StorageVehicle

    constructor(){
        mDAO = DAO()
        mStorage = StorageVehicle()
    }

    override fun obtenerTipos(context: Context) {
        mDAO?.obtenerTipos(context, object: BasicCallback{
            override fun response(event: Any) {
                EventBus.getDefault().post(event as TipoEvent)
            }
        })
    }

    override fun obtenerMarcas(context: Context, idTipo: String) {
        mDAO?.obtenerMarcas(context,idTipo,object: BasicCallback{
            override fun response(event: Any) {
                EventBus.getDefault().post(event as MarcaEvent)
            }
        })
    }

    override fun obtenerModelos(context: Context, idMarca: String) {
        mDAO?.obtenerModelos(context,idMarca,object: BasicCallback{
            override fun response(event: Any) {
                EventBus.getDefault().post(event as ModeloEvent)
            }
        })
    }

    override fun registroVehiculo(
        context: Context,
        vehiculo: Vehiculo,
        mPhotoSelectedUri: Uri?
    ) {
        mDAO?.registroVehiculo(context,vehiculo,object: BasicCallback{
            override fun response(event: Any) {
                val response = event as VehiculoEvent
                if (Util.SUCCESS == response.typeEvent && mPhotoSelectedUri != null){
                    // Almacena la imagen en storage de firebase
                    mStorage.uploadImageReport(
                        imageUri = mPhotoSelectedUri,
                        vehiculo = response.content!!,
                        callback = object: StorageUploadImageCallback {
                            override fun onSuccess(newUri: Uri) {
                                // registro exitoso en firebase storage
                                event.content!!.usuario = vehiculo.usuario!!
                                event.content!!.foto = newUri.toString()
                                mDAO.actualizarRutaImagen(
                                    context,
                                    event.content!!,
                                    object : BasicCallback {
                                        override fun response(eventImage: Any) {
                                            // Si actualizó la ruta de la imagen
                                            //val basicEvent = eventImage as BasicEvent
                                            EventBus.getDefault().post(event)
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
                }else{
                    EventBus.getDefault().post(event as VehiculoEvent)
                }

            }
        })
    }
}