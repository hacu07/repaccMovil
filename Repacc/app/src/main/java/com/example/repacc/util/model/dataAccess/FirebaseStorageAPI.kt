package com.example.repacc.util.model.dataAccess

import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

// Singleton
object FirebaseStorageAPI {

    private var mFirebaseStorage : FirebaseStorage = FirebaseStorage.getInstance()

    fun getmFirebaseStorage(): FirebaseStorage {
        return mFirebaseStorage
    }

    //Obtiene la referencia de la carpeta segun el parametro enviado
    fun getPhotosReference(pathReference: String): StorageReference {
        return mFirebaseStorage.reference.child(pathReference)
    }
}