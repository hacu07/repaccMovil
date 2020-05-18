package com.example.repacc.reportes.model

import android.content.Context

interface ReportesModel {
    fun obtenerReporte(context: Context, idReporte: String)
    fun obtenerListaReportes(context: Context, idMunicipio: String)
}