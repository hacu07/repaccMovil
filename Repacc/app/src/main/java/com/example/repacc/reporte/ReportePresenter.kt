package com.example.repacc.reporte

import android.content.Context
import android.net.Uri
import com.example.repacc.pojo.Reporte

interface ReportePresenter {
    fun onCreate()
    fun onDestroy()

    fun obtenerServicios(context: Context)
    fun registrarReporte(
        context: Context,
        reporte: Reporte,
        mPhotoSelectedUri: Uri?
    )
}