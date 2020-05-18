package com.example.repacc.reportes

import android.content.Context

interface ReportesPresenter {
    fun obtenerReporte(context: Context, idReporte: String)
    fun obtenerListaReportes(context: Context)

    fun onCreate()
    fun onDestroy()
}