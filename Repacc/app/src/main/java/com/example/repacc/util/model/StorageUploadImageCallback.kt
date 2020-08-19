package com.example.repacc.util.model

import android.net.Uri

interface StorageUploadImageCallback {
    fun onSuccess(newUri: Uri)
    fun onError(resMsg: Int)
}