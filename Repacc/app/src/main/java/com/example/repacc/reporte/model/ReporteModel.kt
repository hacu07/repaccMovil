package com.example.repacc.reporte.model

import android.content.Context
import android.net.Uri
import com.example.repacc.pojo.Reporte

interface ReporteModel {
    fun obtenerServicios(context: Context)
    fun registrarReporte(
        context: Context,
        reporte: Reporte,
        mPhotoSelectedUri: Uri?
    )
}