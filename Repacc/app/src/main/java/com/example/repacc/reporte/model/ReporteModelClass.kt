package com.example.repacc.reporte.model

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.repacc.pojo.Reporte
import com.example.repacc.reporte.model.DAO.DAO
import com.example.repacc.reporte.model.DAO.Storage
import com.example.repacc.reportes.events.ReporteEvent
import com.example.repacc.util.BasicCallback
import com.example.repacc.util.BasicEvent
import com.example.repacc.util.Util
import com.example.repacc.util.model.StorageUploadImageCallback
import com.example.repacc.vehiculoAgregar.events.TipoEvent
import org.greenrobot.eventbus.EventBus

class ReporteModelClass: ReporteModel {

    var mDAO: DAO
    lateinit var mStorage: Storage

    constructor(){
        mDAO = DAO()
        mStorage = Storage()
    }

    override fun obtenerServicios(context: Context) {
        mDAO.obtenerServicios(context, object: BasicCallback{
            override fun response(event: Any) {
                postServices(event as TipoEvent)
            }
        })
    }

    fun postServices(tipoEvent: TipoEvent) {
        EventBus.getDefault().post(tipoEvent)
    }

    override fun registrarReporte(
        context: Context,
        reporte: Reporte,
        mPhotoSelectedUri: Uri?
    ) {
        mDAO.registrarReporte(context,reporte,object: BasicCallback{
            override fun response(event: Any) {

                // Si registró correctamente el reporte
                val responseReporte = event as ReporteEvent

                if (Util.SUCCESS == responseReporte.typeEvent && mPhotoSelectedUri != null){
                    // Almacena la imagen en storage de firebase
                    mStorage.uploadImageReport(
                        imageUri = mPhotoSelectedUri,
                        reporte = responseReporte.content!!,
                        callback = object: StorageUploadImageCallback{
                            override fun onSuccess(newUri: Uri) {
                                // registro exitoso en firebase storage
                                responseReporte.content!!.imagen = newUri.toString()
                                mDAO.actualizarImagen(
                                    context,
                                    responseReporte.content!!,
                                    object : BasicCallback{
                                        override fun response(event: Any) {
                                            // Si actualizó la ruta de la imagen
                                            val basicEvent = event as BasicEvent
                                            Log.i("REPACC",basicEvent.msj)
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

                postReporte(responseReporte)
            }
        })
    }

    fun postReporte(basicEvent: ReporteEvent) {
        EventBus.getDefault().post(basicEvent)
    }
}