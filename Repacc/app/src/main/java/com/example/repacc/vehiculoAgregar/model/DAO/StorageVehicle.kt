package com.example.repacc.vehiculoAgregar.model.DAO

import android.net.Uri
import com.example.repacc.R
import com.example.repacc.pojo.Vehiculo
import com.example.repacc.util.Constantes
import com.example.repacc.util.Util
import com.example.repacc.util.model.StorageUploadImageCallback
import com.example.repacc.util.model.dataAccess.FirebaseStorageAPI
import com.google.firebase.storage.StorageReference

class StorageVehicle {
    private val mStorageAPI = FirebaseStorageAPI

    // Carga la imagen del reporte
    fun uploadImageReport(imageUri: Uri, vehiculo: Vehiculo, callback: StorageUploadImageCallback){
        if (imageUri.lastPathSegment != null){
            val photoRef: StorageReference = mStorageAPI.getPhotosReference(Util.STORAGE_REFERENCE_USERS)
                .child(Constantes.config!!.usuario!!.usuario!!).child(Util.PATH_VEHICLES_USER).child(imageUri.lastPathSegment!!)

            // agrega la foto
            photoRef.putFile(imageUri)
                .addOnSuccessListener {
                    // Si Salió exitoso obtiene la url de descarga
                    it.storage.downloadUrl.addOnSuccessListener {uri: Uri? ->
                        // Obtiene la Uri donde se almacenó la imagen
                        if (uri != null){
                            callback.onSuccess(uri)
                        }else{
                            callback.onError(R.string.report_error_imageUpdated);
                        }
                    }
                }
        }else{
            callback.onError(R.string.report_error_invalid_image)
        }
    }
}