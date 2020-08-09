package com.example.repacc.reporteDetalle.view.fragments

import android.content.Context
import com.example.repacc.reporteDetalle.view.fragments.events.EntidadesEvent

interface EntidadFragmentPresenter {
    fun onShow() // Suscribe a eventos
    fun onDestroy()

    fun buscarClinicasMunicipio(context: Context, idMunicipio: String)
    fun onEventListener(event: EntidadesEvent)
}